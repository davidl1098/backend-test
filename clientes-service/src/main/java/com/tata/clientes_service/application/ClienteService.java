package com.tata.clientes_service.application;

import com.tata.clientes_service.web.dto.ClienteDto;

import java.util.UUID;

import org.springframework.data.domain.Page;

public interface ClienteService {

    ClienteDto create(ClienteDto dto);

    ClienteDto get(UUID id);

    ClienteDto update(UUID id, ClienteDto dto);

    void delete(UUID id);

    Page<ClienteDto> list(String q, int page, int size);
}
