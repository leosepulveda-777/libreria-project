package com.librarysystem.service;

import com.librarysystem.dto.AuthResponseDTO;
import com.librarysystem.dto.RegisterRequestDTO;

public interface AuthService {

    // US-001: Registro de lector
    AuthResponseDTO register(RegisterRequestDTO request);
}