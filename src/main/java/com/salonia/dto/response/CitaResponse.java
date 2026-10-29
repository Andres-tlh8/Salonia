package com.salonia.dto.response;

import com.salonia.enums.EstadoCita;

import java.time.LocalDate;
import java.time.LocalTime;

public record CitaResponse(
        Long id,
        LocalDate fecha,
        LocalTime horaInicio,
        LocalTime horaFin,
        EstadoCita estado,
        Long clienteId,
        Long estilistaId,
        Long servicioId,
        Long salonId
) {
}
