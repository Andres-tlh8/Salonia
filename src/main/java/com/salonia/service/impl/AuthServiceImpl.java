package com.salonia.service.impl;

import com.salonia.dto.request.LoginRequest;
import com.salonia.dto.request.RegisterRequest;
import com.salonia.dto.response.AuthResponse;
import com.salonia.entity.Salon;
import com.salonia.entity.Usuario;
import com.salonia.exception.EntityNotFoundException;
import com.salonia.repository.SalonRepository;
import com.salonia.repository.UsuarioRepository;
import com.salonia.security.JwtService;
import com.salonia.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final SalonRepository salonRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        Salon salon = salonRepository.findById(1L).orElseGet(() -> {
            Salon nuevoSalon = new Salon();
            nuevoSalon.setNombre("Salón principal");
            nuevoSalon.setCiudad("Ciudad por defecto");
            nuevoSalon.setDireccion("Dirección por defecto");
            nuevoSalon.setTelefono("0000000000");
            nuevoSalon.setActivo(true);
            return salonRepository.save(nuevoSalon);
        });

        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setRol(request.rol());
        usuario.setSalon(salon);
        usuarioRepository.save(usuario);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities("ROLE_" + usuario.getRol().name())
                .build();

        return new AuthResponse(jwtService.generateToken(userDetails), "Bearer");
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        if (!authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities("ROLE_" + usuario.getRol().name())
                .build();

        return new AuthResponse(jwtService.generateToken(userDetails), "Bearer");
    }
}
