package com.salonia.dto.request;

import com.salonia.enums.EstadoCita;
import jakarta.validation.constraints.NotNull;

public record UpdateEstadoCitaRequest(

        @NotNull(message = "El estado es obligatorio")
        EstadoCita estado

) {
}