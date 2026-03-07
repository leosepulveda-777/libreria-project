package com.librarysystem.controller;

import com.librarysystem.dto.AuthResponseDTO;
import com.librarysystem.dto.LoginRequestDTO;
import com.librarysystem.dto.RefreshTokenRequestDTO;
import com.librarysystem.dto.RegisterRequestDTO;
import com.librarysystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "1-3. Autenticación", description = "Registro, login y renovación de token")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "US-001 – Registro de lector",
            description = "Registra un ciudadano como lector. Asigna rol LECTOR y genera número de carnet único.")
    @ApiResponse(responseCode = "201", description = "Lector registrado exitosamente")
    @ApiResponse(responseCode = "409", description = "Email o documento ya registrado")
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "US-002 – Login",
            description = "Autentica al usuario con email y password. Retorna access_token y refresh_token.")
    @ApiResponse(responseCode = "200", description = "Login exitoso")
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "US-003 – Refresh token",
            description = "Renueva el access_token usando un refresh_token válido.")
    @ApiResponse(responseCode = "200", description = "Token renovado exitosamente")
    @ApiResponse(responseCode = "401", description = "Refresh token inválido o expirado")
    public ResponseEntity<AuthResponseDTO> refreshToken(
            @Valid @RequestBody RefreshTokenRequestDTO request) {
        return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));
    }
}