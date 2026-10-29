package com.salonia.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateEstilistaRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "La especialidad es obligatoria") String especialidad,
        Boolean activo
) {
}
