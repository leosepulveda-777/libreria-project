package com.librarysystem.service.impl;

import com.librarysystem.dto.AuthResponseDTO;
import com.librarysystem.dto.LoginRequestDTO;
import com.librarysystem.dto.RegisterRequestDTO;
import com.librarysystem.entity.Role;
import com.librarysystem.entity.User;
import com.librarysystem.exception.ResourceNotFoundException;
import com.librarysystem.repository.RoleRepository;
import com.librarysystem.repository.UserRepository;
import com.librarysystem.service.AuthService;
import com.librarysystem.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        // Validar que el email no exista
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con este email");
        }

        // Validar que el documento no exista
        if (userRepository.existsByDocument(request.getDocumento())) {
            throw new IllegalArgumentException("Ya existe un usuario con este documento");
        }

        // Obtener rol LECTOR
        Role lectorRole = roleRepository.findByName("LECTOR")
                .orElseThrow(() -> new ResourceNotFoundException("Rol LECTOR no encontrado"));

        // Generar número de carnet único
        String cardNumber;
        do {
            cardNumber = "LIB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (userRepository.findByCardNumber(cardNumber).isPresent());

        // Crear usuario
        User user = User.builder()
                .firstName(request.getNombre().split(" ")[0])
                .lastName(request.getNombre().contains(" ") ?
                    request.getNombre().substring(request.getNombre().indexOf(" ") + 1) :
                    "")
                .document(request.getDocumento())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getClave()))
                .cardNumber(cardNumber)
                .active(true)
                .role(lectorRole)
                .build();

        User savedUser = userRepository.save(user);

        // Generar tokens
        String accessToken = jwtUtil.generateToken(
            savedUser.getEmail(),
            savedUser.getRole().getName(),
            savedUser.getId(),
            savedUser.getCardNumber()
        );

        String refreshToken = jwtUtil.generateRefreshToken(savedUser.getEmail());

        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(savedUser.getId())
                .rol(savedUser.getRole().getName())
                .numeroCarnet(savedUser.getCardNumber())
                .nombre(savedUser.getFirstName() + " " + savedUser.getLastName())
                .email(savedUser.getEmail())
                .build();
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        // Buscar usuario por email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        // Verificar contraseña
        if (!passwordEncoder.matches(request.getClave(), user.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        // Verificar que el usuario esté activo
        if (!user.getActive()) {
            throw new IllegalArgumentException("Usuario inactivo");
        }

        // Generar tokens
        String accessToken = jwtUtil.generateToken(
            user.getEmail(),
            user.getRole().getName(),
            user.getId(),
            user.getCardNumber()
        );

        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(user.getId())
                .rol(user.getRole().getName())
                .numeroCarnet(user.getCardNumber())
                .nombre(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .build();
    }

    @Override
    public AuthResponseDTO refreshToken(String refreshToken) {
        try {
            // Validar refresh token
            if (jwtUtil.isTokenExpired(refreshToken)) {
                throw new IllegalArgumentException("Refresh token expirado");
            }

            String email = jwtUtil.getUsernameFromToken(refreshToken);

            // Buscar usuario
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            // Verificar que el usuario esté activo
            if (!user.getActive()) {
                throw new IllegalArgumentException("Usuario inactivo");
            }

            // Generar nuevos tokens
            String newAccessToken = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().getName(),
                user.getId(),
                user.getCardNumber()
            );

            String newRefreshToken = jwtUtil.generateRefreshToken(user.getEmail());

            return AuthResponseDTO.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .userId(user.getId())
                    .rol(user.getRole().getName())
                    .numeroCarnet(user.getCardNumber())
                    .nombre(user.getFirstName() + " " + user.getLastName())
                    .email(user.getEmail())
                    .build();

        } catch (Exception e) {
            throw new IllegalArgumentException("Refresh token inválido");
        }
    }
}