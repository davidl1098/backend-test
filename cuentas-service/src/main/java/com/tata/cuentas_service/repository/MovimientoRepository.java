package com.tata.cuentas_service.repository;

import com.tata.cuentas_service.domain.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface MovimientoRepository extends JpaRepository<Movimiento, UUID> {
    List<Movimiento> findByCuentaIdAndFechaBetweenOrderByFechaAsc(
            UUID cuentaId, OffsetDateTime desde, OffsetDateTime hasta);
}
