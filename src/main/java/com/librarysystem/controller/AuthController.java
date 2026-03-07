package com.librarysystem.controller;

import com.librarysystem.dto.AuthResponseDTO;
import com.librarysystem.dto.RegisterRequestDTO;
import com.librarysystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")  // FIX: era /auth
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "US-001 – Registro de lector")
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(request));
    }
}