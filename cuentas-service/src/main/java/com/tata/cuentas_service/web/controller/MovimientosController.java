package com.tata.cuentas_service.web.controller;

import com.tata.cuentas_service.application.MovimientoService;
import com.tata.cuentas_service.web.dto.MovimientoDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientosController {
    private final MovimientoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoDto registrar(@RequestBody @Valid MovimientoDto dto) {
        return service.registrar(dto);
    }
}
