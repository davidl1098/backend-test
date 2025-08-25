package com.tata.clientes_service.web.dto;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record PersonaDto(
                UUID id,
                @NotBlank @Size(max = 120) String nombre,
                @NotBlank @Size(max = 10) String genero,
                @NotNull @Min(0) @Max(130) Integer edad,
                @NotBlank @Size(max = 20) String identificacion,
                @NotBlank @Size(max = 160) String direccion,
                @NotBlank @Size(max = 20) String telefono) {
}
