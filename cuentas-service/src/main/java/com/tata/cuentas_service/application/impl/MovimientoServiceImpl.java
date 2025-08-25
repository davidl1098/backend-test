package com.tata.cuentas_service.application.impl;

import com.tata.cuentas_service.application.MovimientoService;
import com.tata.cuentas_service.domain.Movimiento;
import com.tata.cuentas_service.web.dto.MovimientoDto;
import com.tata.cuentas_service.repository.CuentaRepository;
import com.tata.cuentas_service.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class MovimientoServiceImpl implements MovimientoService {

    private final CuentaRepository cuentaRepo;
    private final MovimientoRepository movRepo;

    @Override
    public MovimientoDto registrar(MovimientoDto dto) {
        var cuenta = cuentaRepo.findById(dto.cuentaId()).orElseThrow(NoSuchElementException::new);
        if (Boolean.FALSE.equals(cuenta.getEstado()))
            throw new IllegalArgumentException("Cuenta inactiva");

        var valor = dto.valor();
        if (valor.signum() == 0)
            throw new IllegalArgumentException("El valor del movimiento no puede ser 0");

        var nuevoSaldo = cuenta.getSaldo().add(valor);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Saldo no disponible");

        cuenta.setSaldo(nuevoSaldo);
        cuentaRepo.save(cuenta);

        var tipo = valor.signum() >= 0 ? "CREDITO" : "DEBITO";
        var mov = Movimiento.builder()
                .cuenta(cuenta)
                .tipo(tipo)
                .valor(valor)
                .saldoResultante(nuevoSaldo)
                .build();

        var saved = movRepo.save(mov);
        return new MovimientoDto(saved.getId(), cuenta.getId(), saved.getFecha(), saved.getValor(), saved.getTipo(),
                saved.getSaldoResultante());
    }
}
