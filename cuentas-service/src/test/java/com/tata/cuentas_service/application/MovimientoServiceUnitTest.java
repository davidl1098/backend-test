package com.tata.cuentas_service.application;

import com.tata.cuentas_service.application.impl.MovimientoServiceImpl;
import com.tata.cuentas_service.domain.Cuenta;
import com.tata.cuentas_service.web.dto.MovimientoDto;
import com.tata.cuentas_service.repository.CuentaRepository;
import com.tata.cuentas_service.repository.MovimientoRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovimientoServiceUnitTest {

    @Test
    void debito_que_deja_saldo_negativo_debe_fallar() {
        var cuentaRepo = mock(CuentaRepository.class);
        var movRepo = mock(MovimientoRepository.class);
        var service = new MovimientoServiceImpl(cuentaRepo, movRepo);

        var cuenta = Cuenta.builder()
                .id(UUID.randomUUID())
                .saldo(new BigDecimal("100.00"))
                .estado(true)
                .build();

        when(cuentaRepo.findById(cuenta.getId())).thenReturn(Optional.of(cuenta));

        var retiro = new MovimientoDto(null, cuenta.getId(), null, new BigDecimal("-150.00"), null, null);

        assertThatThrownBy(() -> service.registrar(retiro))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Saldo no disponible");

        verify(movRepo, never()).save(any());
    }

    @Test
    void credito_incrementa_saldo_y_guarda_movimiento() {
        var cuentaRepo = mock(CuentaRepository.class);
        var movRepo = mock(MovimientoRepository.class);
        var service = new MovimientoServiceImpl(cuentaRepo, movRepo);

        var cuenta = Cuenta.builder()
                .id(UUID.randomUUID())
                .saldo(new BigDecimal("50.00"))
                .estado(true)
                .build();

        when(cuentaRepo.findById(cuenta.getId())).thenReturn(Optional.of(cuenta));
        when(cuentaRepo.save(any(Cuenta.class))).thenAnswer(i -> i.getArgument(0));
        when(movRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        var abono = new MovimientoDto(null, cuenta.getId(), null, new BigDecimal("25.00"), null, null);

        var result = service.registrar(abono);

        assertThat(result.saldoResultante()).isEqualByComparingTo("75.00");

        assertThat(cuenta.getSaldo()).isEqualByComparingTo("75.00");
        verify(movRepo, times(1)).save(any());
    }
}
