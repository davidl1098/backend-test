package com.tata.clientes_service.web.dto;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record ClienteDto(UUID id, @NotBlank String clienteId, @NotBlank String nombre,
                @NotBlank String genero, @NotNull Integer edad, @NotBlank String identificacion,
                String direccion, String telefono, @NotNull Boolean estado,
                @Size(min = 4) String contrasenia) {
}
