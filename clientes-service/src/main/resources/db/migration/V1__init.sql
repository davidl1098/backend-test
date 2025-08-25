
CREATE SCHEMA IF NOT EXISTS clientes;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS clientes.persona (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  nombre          VARCHAR(120) NOT NULL,
  genero          VARCHAR(10)  NOT NULL,
  edad            INTEGER      NOT NULL CHECK (edad >= 0),
  identificacion  VARCHAR(20)  NOT NULL UNIQUE,
  direccion       VARCHAR(160) NOT NULL,
  telefono        VARCHAR(20)  NOT NULL
);

CREATE INDEX IF NOT EXISTS ix_persona_nombre ON clientes.persona(nombre);
CREATE INDEX IF NOT EXISTS ix_persona_identificacion ON clientes.persona(identificacion);

CREATE TABLE IF NOT EXISTS clientes.cliente (
  id               UUID PRIMARY KEY REFERENCES clientes.persona(id) ON DELETE CASCADE,
  cliente_id       VARCHAR(64) NOT NULL UNIQUE,
  contrasenia_hash VARCHAR(120) NOT NULL,
  estado           BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS ix_cliente_estado ON clientes.cliente(estado);
CREATE INDEX IF NOT EXISTS ix_cliente_clienteid ON clientes.cliente(cliente_id);
