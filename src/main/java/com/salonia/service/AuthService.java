package com.salonia.service;

import com.salonia.dto.request.LoginRequest;
import com.salonia.dto.request.RegisterRequest;
import com.salonia.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
