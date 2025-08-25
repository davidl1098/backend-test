package com.tata.cuentas_service.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record ReporteDto(
        String clienteId,
        LocalDate desde,
        LocalDate hasta,
        List<CuentaReporte> cuentas) {
    public record CuentaReporte(
            String numero,
            String tipo,
            Boolean estado,
            BigDecimal saldo,
            List<MovimientoLinea> movimientos) {
    }

    public record MovimientoLinea(
            OffsetDateTime fecha,
            String tipo,
            BigDecimal valor,
            BigDecimal saldo) {
    }
}
