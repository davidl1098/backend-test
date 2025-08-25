package com.tata.cuentas_service.application.impl;

import com.tata.cuentas_service.application.ReporteService;
import com.tata.cuentas_service.web.dto.ReporteDto;
import com.tata.cuentas_service.repository.CuentaRepository;
import com.tata.cuentas_service.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {

    private final CuentaRepository cuentaRepo;
    private final MovimientoRepository movRepo;

    @Override
    public ReporteDto estadoCuenta(String clienteId, LocalDate desde, LocalDate hasta) {
        var cuentas = cuentaRepo.findByClienteId(clienteId).stream().map(cta -> {
            var desdeTs = desde.atStartOfDay().atOffset(OffsetDateTime.now().getOffset());
            var hastaTs = hasta.plusDays(1).atStartOfDay().atOffset(OffsetDateTime.now().getOffset());
            var movs = movRepo.findByCuentaIdAndFechaBetweenOrderByFechaAsc(cta.getId(), desdeTs, hastaTs).stream()
                    .map(m -> new ReporteDto.MovimientoLinea(m.getFecha(), m.getTipo(), m.getValor(),
                            m.getSaldoResultante()))
                    .toList();
            return new ReporteDto.CuentaReporte(cta.getNumero(), cta.getTipo(), cta.getEstado(), cta.getSaldo(), movs);
        }).toList();

        return new ReporteDto(clienteId, desde, hasta, cuentas);
    }
}
