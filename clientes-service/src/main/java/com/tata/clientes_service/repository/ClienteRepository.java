package com.tata.clientes_service.repository;

import com.tata.clientes_service.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
  Optional<Cliente> findByClienteId(String clienteId);

  boolean existsByClienteId(String clienteId);

  Page<Cliente> findByNombreContainingIgnoreCaseOrIdentificacionContainingIgnoreCase(String n, String i, Pageable p);
}