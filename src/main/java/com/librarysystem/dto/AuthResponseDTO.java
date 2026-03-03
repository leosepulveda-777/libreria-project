package com.librarysystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    // El token principal que el usuario usa en cada request
    private String accessToken;

    // El token para renovar la sesión cuando el accessToken vence (US-003)
    private String refreshToken;

    // Info básica del usuario para que el frontend la muestre
    private Long userId;
    private String email;
    private String rol;

    // Solo viene si el usuario es LECTOR, null para ADMIN y BIBLIOTECARIO
    private String numeroCarnet;
}