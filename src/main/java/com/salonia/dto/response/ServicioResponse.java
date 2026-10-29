package com.salonia.dto.response;

public record ServicioResponse(Long id, String nombre, String descripcion, Integer duracionMinutos, Double precio, Long salonId) {
}
