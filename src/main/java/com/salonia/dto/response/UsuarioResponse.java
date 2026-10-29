package com.salonia.dto.response;

import com.salonia.enums.Rol;

public record UsuarioResponse(Long id, String nombre, String email, Rol rol) {
}
