CREATE SCHEMA IF NOT EXISTS cuentas;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Cuentas
CREATE TABLE IF NOT EXISTS cuentas.cuenta (
  id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  numero     VARCHAR(32) NOT NULL UNIQUE,
  tipo       VARCHAR(20) NOT NULL,         -- "Ahorro" / "Corriente" (texto simple)
  saldo      NUMERIC(18,2) NOT NULL DEFAULT 0,
  estado     BOOLEAN NOT NULL DEFAULT TRUE,
  cliente_id VARCHAR(64) NOT NULL
);
CREATE INDEX IF NOT EXISTS ix_cuenta_cliente ON cuentas.cuenta(cliente_id);
CREATE INDEX IF NOT EXISTS ix_cuenta_estado  ON cuentas.cuenta(estado);

-- Movimientos
CREATE TABLE IF NOT EXISTS cuentas.movimiento (
  id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  cuenta_id        UUID NOT NULL REFERENCES cuentas.cuenta(id) ON DELETE CASCADE,
  fecha            TIMESTAMPTZ NOT NULL DEFAULT now(),
  tipo             VARCHAR(20) NOT NULL,                    -- "DEBITO" / "CREDITO" (derivado del signo)
  valor            NUMERIC(18,2) NOT NULL,                  -- puede ser positivo o negativo (como pide F2)
  saldo_resultante NUMERIC(18,2) NOT NULL
);
CREATE INDEX IF NOT EXISTS ix_mov_cta_fecha ON cuentas.movimiento(cuenta_id, fecha);
