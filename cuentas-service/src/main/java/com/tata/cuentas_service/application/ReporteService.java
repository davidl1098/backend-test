package com.tata.cuentas_service.application;

import com.tata.cuentas_service.web.dto.ReporteDto;
import java.time.LocalDate;

public interface ReporteService {
    ReporteDto estadoCuenta(String clienteId, LocalDate desde, LocalDate hasta);
}
