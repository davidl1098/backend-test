package com.tata.cuentas_service.web.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record CuentaDto(
        UUID id,
        @NotBlank String numero,
        @NotBlank String tipo,
        @NotBlank String clienteId,
        @NotNull Boolean estado,
        @NotNull @PositiveOrZero BigDecimal saldo) {
}
