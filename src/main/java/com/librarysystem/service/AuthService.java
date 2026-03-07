package com.librarysystem.service;

import com.librarysystem.dto.AuthResponseDTO;
import com.librarysystem.dto.LoginRequestDTO;
import com.librarysystem.dto.RegisterRequestDTO;

public interface AuthService {

    AuthResponseDTO register(RegisterRequestDTO request);

    AuthResponseDTO login(LoginRequestDTO request);

    AuthResponseDTO refreshToken(String refreshToken);
}