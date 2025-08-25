package com.tata.cuentas_service.web.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record MovimientoDto(
        UUID id,
        @NotNull UUID cuentaId,
        OffsetDateTime fecha,
        @NotNull BigDecimal valor, 
        String tipo, 
        BigDecimal saldoResultante) {
}
