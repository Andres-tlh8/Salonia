package com.salonia.dto.request;

public record UpdateEstilistaRequest(
        String nombre,
        String especialidad,
        Boolean activo
) {
}
