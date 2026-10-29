package com.salonia.dto.request;

import jakarta.validation.constraints.Email;

public record UpdateClienteRequest(
        String nombre,
        String telefono,
        @Email(message = "El email debe ser válido") String email
) {
}
