package com.librarysystem.service;

import com.librarysystem.dto.AuthResponseDTO;
import com.librarysystem.dto.LoginRequestDTO;
import com.librarysystem.dto.RegisterRequestDTO;

public interface AuthService {

    // US-001: Registro de lector
    AuthResponseDTO register(RegisterRequestDTO request);

    // US-002: Login con email y password
    AuthResponseDTO login(LoginRequestDTO request);
    AuthResponseDTO refreshToken(String refreshToken);
}