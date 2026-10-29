package com.salonia.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateCitaRequest(
        LocalDate fecha,
        LocalTime horaInicio,
        Long clienteId,
        Long estilistaId,
        Long servicioId
) {
}
