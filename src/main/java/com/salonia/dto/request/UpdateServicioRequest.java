package com.salonia.dto.request;

import jakarta.validation.constraints.Positive;

public record UpdateServicioRequest(
        String nombre,
        String descripcion,
        @Positive(message = "La duración debe ser positiva") Integer duracionMinutos,
        @Positive(message = "El precio debe ser positivo") Double precio
) {
}
