-- =========================================================
-- BaseDatos.sql  (PostgreSQL 13+)
-- Esquemas: clientes (Persona/Cliente) y cuentas (Cuenta/Movimiento)
-- =========================================================

-- Extensiones útiles
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =========================================================
-- ESQUEMA: clientes
-- =========================================================
CREATE SCHEMA IF NOT EXISTS clientes;

-- PERSONA
-- Requisitos: nombre, genero, edad, identificación, dirección, teléfono; PK propia
-- (identificación única para facilitar búsquedas)
CREATE TABLE IF NOT EXISTS clientes.persona (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  nombre          VARCHAR(120) NOT NULL,
  genero          VARCHAR(10)  NOT NULL,
  edad            INTEGER      NOT NULL CHECK (edad >= 0),
  identificacion  VARCHAR(20)  NOT NULL UNIQUE,
  direccion       VARCHAR(160) NOT NULL,
  telefono        VARCHAR(20)  NOT NULL,
  creado_en       TIMESTAMPTZ  NOT NULL DEFAULT now(),
  actualizado_en  TIMESTAMPTZ  NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS ix_persona_nombre ON clientes.persona(nombre);

-- CLIENTE
-- Requisitos: hereda de Persona (lo modelamos 1:1 vía FK única), clienteId, contraseña, estado; PK propia
CREATE TABLE IF NOT EXISTS clientes.cliente (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  persona_id      UUID NOT NULL UNIQUE
                    REFERENCES clientes.persona(id) ON DELETE CASCADE,
  cliente_id      VARCHAR(32) NOT NULL UNIQUE,
  contrasena      VARCHAR(120) NOT NULL,
  estado          BOOLEAN     NOT NULL DEFAULT TRUE,
  creado_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
  actualizado_en  TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS ix_cliente_estado ON clientes.cliente(estado);

-- =========================================================
-- ESQUEMA: cuentas
-- =========================================================
CREATE SCHEMA IF NOT EXISTS cuentas;

-- CUENTA
-- Requisitos: número cuenta (único), tipo, saldo Inicial, estado; PK propia.
-- Nota: cliente_id se almacena como texto para desacoplar DBs entre microservicios
CREATE TABLE IF NOT EXISTS cuentas.cuenta (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  numero          VARCHAR(32)  NOT NULL UNIQUE,
  tipo            VARCHAR(20)  NOT NULL,                         -- p.ej. 'Ahorro' | 'Corriente'
  saldo           NUMERIC(18,2) NOT NULL DEFAULT 0,
  estado          BOOLEAN       NOT NULL DEFAULT TRUE,
  cliente_id      VARCHAR(64)   NOT NULL,                        -- referencia lógica al cliente
  creado_en       TIMESTAMPTZ   NOT NULL DEFAULT now(),
  actualizado_en  TIMESTAMPTZ   NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS ix_cuenta_cliente ON cuentas.cuenta(cliente_id);
CREATE INDEX IF NOT EXISTS ix_cuenta_estado  ON cuentas.cuenta(estado);

-- MOVIMIENTO
-- Requisitos: fecha, tipo movimiento, valor (+/-), saldo (saldo_resultante), PK propia
-- Regla F2/ F3 se implementa en la capa de servicio; aquí dejamos el histórico preparado.
CREATE TABLE IF NOT EXISTS cuentas.movimiento (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  cuenta_id         UUID NOT NULL
                      REFERENCES cuentas.cuenta(id) ON DELETE CASCADE,
  fecha             TIMESTAMPTZ NOT NULL DEFAULT now(),
  tipo              VARCHAR(20) NOT NULL,                        -- 'CREDITO' | 'DEBITO' (derivado del signo en la app)
  valor             NUMERIC(18,2) NOT NULL,                      -- puede ser positivo o negativo (F2)
  saldo_resultante  NUMERIC(18,2) NOT NULL
);
CREATE INDEX IF NOT EXISTS ix_mov_cta_fecha ON cuentas.movimiento(cuenta_id, fecha);

-- =========================================================
-- DATOS DE EJEMPLO (tomados de los casos del PDF)
-- =========================================================

-- Personas (nombre, genero, edad, identificacion, direccion, telefono)
INSERT INTO clientes.persona (id, nombre, genero, edad, identificacion, direccion, telefono)
VALUES
  (gen_random_uuid(), 'Jose Lema',      'M', 35, '01010101', 'Otavalo sn y principal',  '098254785'),
  (gen_random_uuid(), 'Marianela Montalvo','F', 32, '02020202', 'Amazonas y  NNUU',     '097548965'),
  (gen_random_uuid(), 'Juan Osorio',    'M', 28, '03030303', '13 junio y Equinoccial',  '098874587')
ON CONFLICT DO NOTHING;

-- Clientes (1:1 con persona). ClienteId de ejemplo y contraseñas en texto plano aquí;
-- en la app se cifran con PasswordEncoder.
WITH pers AS (
  SELECT p.id, p.nombre FROM clientes.persona p
)
INSERT INTO clientes.cliente (persona_id, cliente_id, contrasena, estado)
SELECT id,
       CASE nombre
         WHEN 'Jose Lema' THEN 'CLI-LEMA'
         WHEN 'Marianela Montalvo' THEN 'CLI-MARI'
         WHEN 'Juan Osorio' THEN 'CLI-JOSO'
       END AS cliente_id,
       CASE nombre
         WHEN 'Jose Lema' THEN '1234'
         WHEN 'Marianela Montalvo' THEN '5678'
         WHEN 'Juan Osorio' THEN '1245'
       END AS contrasena,
       TRUE
FROM pers
ON CONFLICT DO NOTHING;

-- Cuentas (numero, tipo, saldo inicial, estado, cliente_id lógico)
INSERT INTO cuentas.cuenta (numero, tipo, saldo, estado, cliente_id)
VALUES
  ('478758', 'Ahorro',    2000, TRUE, 'CLI-LEMA'),
  ('225487', 'Corriente',  100, TRUE, 'CLI-MARI'),
  ('495878', 'Ahorros',      0, TRUE, 'CLI-JOSO'),
  ('496825', 'Ahorros',    540, TRUE, 'CLI-MARI')
ON CONFLICT DO NOTHING;

-- Cuenta adicional (ejemplo del PDF)
INSERT INTO cuentas.cuenta (numero, tipo, saldo, estado, cliente_id)
VALUES ('585545', 'Corriente', 1000, TRUE, 'CLI-LEMA')
ON CONFLICT DO NOTHING;

-- Movimientos de ejemplo (los saldos resultantes aquí son ilustrativos; en la app se recalculan):
-- 478758 (Ahorro 2000) -> Retiro 575  => saldo 1425
WITH cta AS (SELECT id FROM cuentas.cuenta WHERE numero='478758')
INSERT INTO cuentas.movimiento (cuenta_id, tipo, valor, saldo_resultante)
SELECT id, 'DEBITO', -575, 1425 FROM cta;

-- 225487 (Corriente 100) -> Depósito 600 => saldo 700
WITH cta AS (SELECT id FROM cuentas.cuenta WHERE numero='225487')
INSERT INTO cuentas.movimiento (cuenta_id, tipo, valor, saldo_resultante)
SELECT id, 'CREDITO', 600, 700 FROM cta;

-- 495878 (Ahorros 0) -> Depósito 150 => saldo 150
WITH cta AS (SELECT id FROM cuentas.cuenta WHERE numero='495878')
INSERT INTO cuentas.movimiento (cuenta_id, tipo, valor, saldo_resultante)
SELECT id, 'CREDITO', 150, 150 FROM cta;

-- 496825 (Ahorros 540) -> Retiro 540 => saldo 0
WITH cta AS (SELECT id FROM cuentas.cuenta WHERE numero='496825')
INSERT INTO cuentas.movimiento (cuenta_id, tipo, valor, saldo_resultante)
SELECT id, 'DEBITO', -540, 0 FROM cta;

