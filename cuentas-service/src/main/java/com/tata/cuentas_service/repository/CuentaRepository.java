package com.tata.cuentas_service.repository;

import com.tata.cuentas_service.domain.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CuentaRepository extends JpaRepository<Cuenta, UUID> {
    Optional<Cuenta> findByNumero(String numero);

    boolean existsByNumero(String numero);

    List<Cuenta> findByClienteId(String clienteId);
}
