package com.tata.clientes_service.application.impl;

import com.tata.clientes_service.application.ClienteService;
import com.tata.clientes_service.domain.Cliente;
import com.tata.clientes_service.repository.ClienteRepository;
import com.tata.clientes_service.repository.PersonaRepository;
import com.tata.clientes_service.web.dto.ClienteDto;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository clienteRepo;
    private final PersonaRepository personaRepo;
    private final PasswordEncoder encoder;
    private final ClienteEventPublisher events;

    public ClienteDto create(ClienteDto dto) {
        validarUnicos(dto, null);
        if (dto.contrasenia() == null || dto.contrasenia().isBlank())
            throw new IllegalArgumentException("La contraseña es requerida");
        var c = toEntity(dto);
        c.setContraseniaHash(encoder.encode(dto.contrasenia()));
        var saved = clienteRepo.save(c);
        events.emitCreated(saved);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public ClienteDto get(UUID id) {
        return toDto(clienteRepo.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado")));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClienteDto> list(String q, int page, int size) {
        var p = PageRequest.of(page, size, Sort.by("nombre").ascending());
        var res = (q == null || q.isBlank()) ? clienteRepo.findAll(p)
                : clienteRepo.findByNombreContainingIgnoreCaseOrIdentificacionContainingIgnoreCase(q, q, p);
        return res.map(this::toDto);
    }

    @Override
    public ClienteDto update(UUID id, ClienteDto dto) {
        var c = clienteRepo.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        validarUnicos(dto, id);
        c.setClienteId(dto.clienteId());
        c.setNombre(dto.nombre());
        c.setGenero(dto.genero());
        c.setEdad(dto.edad());
        c.setIdentificacion(dto.identificacion());
        c.setDireccion(dto.direccion());
        c.setTelefono(dto.telefono());
        c.setEstado(dto.estado() != null ? dto.estado() : c.getEstado());
        if (dto.contrasenia() != null && !dto.contrasenia().isBlank())
            c.setContraseniaHash(encoder.encode(dto.contrasenia()));
        var saved = clienteRepo.save(c);
        events.emitUpdated(saved);
        return toDto(saved);
    }

    public void delete(UUID id) {
        var c = clienteRepo.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        clienteRepo.deleteById(id);
        events.emitDeleted(c);
    }

    private void validarUnicos(ClienteDto dto, UUID selfId) {
        if (dto.clienteId() != null && clienteRepo.existsByClienteId(dto.clienteId())) {
            if (selfId == null || !clienteRepo.findById(selfId)
                    .map(x -> dto.clienteId().equalsIgnoreCase(x.getClienteId())).orElse(false))
                throw new RuntimeException("clienteId en uso");
        }
        if (dto.identificacion() != null && personaRepo.existsByIdentificacion(dto.identificacion())) {
            if (selfId == null || !clienteRepo.findById(selfId)
                    .map(x -> dto.identificacion().equalsIgnoreCase(x.getIdentificacion())).orElse(false))
                throw new RuntimeException("identificación en uso");
        }
    }

    private Cliente toEntity(ClienteDto d) {
        var c = new Cliente();
        c.setId(d.id());
        c.setClienteId(d.clienteId());
        c.setNombre(d.nombre());
        c.setGenero(d.genero());
        c.setEdad(d.edad());
        c.setIdentificacion(d.identificacion());
        c.setDireccion(d.direccion());
        c.setTelefono(d.telefono());
        c.setEstado(d.estado() != null ? d.estado() : Boolean.TRUE);
        return c;
    }

    private ClienteDto toDto(Cliente c) {
        return new ClienteDto(c.getId(), c.getClienteId(), c.getNombre(), c.getGenero(), c.getEdad(),
                c.getIdentificacion(), c.getDireccion(), c.getTelefono(), c.getEstado(), null);
    }

}
