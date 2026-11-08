package com.salonia.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salonia.enums.EstadoCita;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

public record CitaResponse(

        Long id,

        LocalDate fecha,

        @JsonFormat(pattern = "HH:mm")
        @Schema(type = "string", example = "14:30")
        LocalTime horaInicio,

        @JsonFormat(pattern = "HH:mm")
        @Schema(type = "string", example = "15:30")
        LocalTime horaFin,

        EstadoCita estado,

        Long clienteId,

        Long estilistaId,

        Long servicioId,

        Long salonId

) {
}