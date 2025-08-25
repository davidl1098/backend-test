package com.tata.cuentas_service.web.controller;

import com.tata.cuentas_service.application.CuentaService;
import com.tata.cuentas_service.web.dto.CuentaDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentasController {
    private final CuentaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuentaDto create(@RequestBody @Valid CuentaDto dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public CuentaDto get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public CuentaDto update(@PathVariable UUID id, @RequestBody @Valid CuentaDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping
    public List<CuentaDto> list() {
        return service.list();
    }
}
