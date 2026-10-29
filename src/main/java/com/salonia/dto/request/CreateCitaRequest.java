package com.salonia.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateCitaRequest(
        @NotNull(message = "La fecha es obligatoria") LocalDate fecha,
        @NotNull(message = "La hora de inicio es obligatoria") LocalTime horaInicio,
        @NotNull(message = "El cliente es obligatorio") Long clienteId,
        @NotNull(message = "El estilista es obligatorio") Long estilistaId,
        @NotNull(message = "El servicio es obligatorio") Long servicioId
) {
}
