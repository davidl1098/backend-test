package com.tata.clientes_service.web;

import com.tata.clientes_service.application.ClienteService;
import com.tata.clientes_service.web.dto.ClienteDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClientesController {
  private final ClienteService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ClienteDto create(@RequestBody @Valid ClienteDto d) {
    return service.create(d);
  }

  @GetMapping("/{id}")
  public ClienteDto get(@PathVariable UUID id) {
    return service.get(id);
  }

  @GetMapping
  public Page<ClienteDto> list(@RequestParam(required = false) String q, @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return service.list(q, page, size);
  }

  @PutMapping("/{id}")
  public ClienteDto update(@PathVariable UUID id, @RequestBody @Valid ClienteDto d) {
    return service.update(id, d);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    service.delete(id);
  }
}
