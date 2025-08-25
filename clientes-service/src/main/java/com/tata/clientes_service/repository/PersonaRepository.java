package com.tata.clientes_service.repository;

import com.tata.clientes_service.domain.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface PersonaRepository extends JpaRepository<Persona, UUID> {
  Optional<Persona> findByIdentificacion(String identificacion);

  boolean existsByIdentificacion(String identificacion);
}
