package com.librarysystem.controller;

import com.librarysystem.dto.AuthResponseDTO;
import com.librarysystem.dto.RegisterRequestDTO;
import com.librarysystem.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // US-001: Registro de lector
    // @Valid activa las validaciones del RegisterRequestDTO (@NotBlank, @Email, etc.)
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        AuthResponseDTO response = authService.register(request);
        // 201 Created es el código correcto cuando se crea un recurso nuevo
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}