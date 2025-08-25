package com.tata.cuentas_service.web.controller;

import com.tata.cuentas_service.application.ReporteService;
import com.tata.cuentas_service.web.dto.ReporteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReportesController {
    private final ReporteService service;

    // /api/reportes?clienteId=CLI-100&desde=2025-02-01&hasta=2025-02-10
    @GetMapping
    public ReporteDto estado(
            @RequestParam String clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return service.estadoCuenta(clienteId, desde, hasta);
    }
}
