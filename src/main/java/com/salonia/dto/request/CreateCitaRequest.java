package com.salonia.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateCitaRequest(

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        @NotNull(message = "La hora de inicio es obligatoria")
        @JsonFormat(pattern = "HH:mm")
        @Schema(type = "string", example = "14:30")
        LocalTime horaInicio,

        @NotNull(message = "El cliente es obligatorio")
        Long clienteId,

        @NotNull(message = "El estilista es obligatorio")
        Long estilistaId,

        @NotNull(message = "El servicio es obligatorio")
        Long servicioId

) {
}