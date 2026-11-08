package com.salonia.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateCitaRequest(

        LocalDate fecha,

        @JsonFormat(pattern = "HH:mm")
        @Schema(type = "string", example = "14:30")
        LocalTime horaInicio,

        Long clienteId,

        Long estilistaId,

        Long servicioId

) {
}