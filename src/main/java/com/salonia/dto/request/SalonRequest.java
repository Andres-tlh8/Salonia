package com.salonia.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SalonRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "La ciudad es obligatoria") String ciudad,
        @NotBlank(message = "La dirección es obligatoria") String direccion,
        @NotBlank(message = "El teléfono es obligatorio") String telefono,
        Boolean activo
) {
}
