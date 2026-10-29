package com.salonia.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateClienteRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El teléfono es obligatorio") String telefono,
        @NotBlank(message = "El email es obligatorio") @Email(message = "El email debe ser válido") String email
) {
}
