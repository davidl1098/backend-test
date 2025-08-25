package com.tata.clientes_service.web.dto;

import java.time.Instant;
import java.util.UUID;

public record ClienteEventDto(
  String eventId, String eventType, Instant at,
  UUID id, String clienteId, String nombre, String genero, Integer edad,
  String identificacion, String direccion, String telefono, Boolean estado
) {}
