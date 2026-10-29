package com.salonia.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateServicioRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "La descripción es obligatoria") String descripcion,
        @NotNull(message = "La duración es obligatoria") @Positive(message = "La duración debe ser positiva") Integer duracionMinutos,
        @NotNull(message = "El precio es obligatorio") @Positive(message = "El precio debe ser positivo") Double precio
) {
}
