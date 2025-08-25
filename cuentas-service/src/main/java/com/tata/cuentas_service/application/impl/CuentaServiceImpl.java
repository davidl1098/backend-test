package com.tata.cuentas_service.application.impl;

import com.tata.cuentas_service.application.CuentaService;
import com.tata.cuentas_service.domain.Cuenta;
import com.tata.cuentas_service.web.dto.CuentaDto;
import com.tata.cuentas_service.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository repo;

    @Override
    public CuentaDto create(CuentaDto dto) {
        if (repo.existsByNumero(dto.numero()))
            throw new DataIntegrityViolationException("Número de cuenta en uso");
        var c = Cuenta.builder()
                .numero(dto.numero())
                .tipo(dto.tipo())
                .clienteId(dto.clienteId())
                .estado(dto.estado())
                .saldo(dto.saldo())
                .build();
        return toDto(repo.save(c));
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaDto get(UUID id) {
        return toDto(repo.findById(id).orElseThrow(NoSuchElementException::new));
    }

    @Override
    public CuentaDto update(UUID id, CuentaDto dto) {
        var c = repo.findById(id).orElseThrow(NoSuchElementException::new);
        if (!c.getNumero().equals(dto.numero()) && repo.existsByNumero(dto.numero()))
            throw new DataIntegrityViolationException("Número de cuenta en uso");
        c.setNumero(dto.numero());
        c.setTipo(dto.tipo());
        c.setClienteId(dto.clienteId());
        c.setEstado(dto.estado());
        return toDto(repo.save(c));
    }

    @Override
    public void delete(UUID id) {
        if (!repo.existsById(id))
            throw new NoSuchElementException();
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaDto> list() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    private CuentaDto toDto(Cuenta c) {
        return new CuentaDto(c.getId(), c.getNumero(), c.getTipo(), c.getClienteId(), c.getEstado(), c.getSaldo());
    }
}
