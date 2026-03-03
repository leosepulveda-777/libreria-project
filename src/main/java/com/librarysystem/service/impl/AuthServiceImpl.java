package com.librarysystem.service.impl;

import com.librarysystem.dto.AuthResponseDTO;
import com.librarysystem.dto.LoginRequestDTO;
import com.librarysystem.dto.RegisterRequestDTO;
import com.librarysystem.entity.Role;
import com.librarysystem.entity.RoleEnum;
import com.librarysystem.entity.User;
import com.librarysystem.repository.RoleRepository;
import com.librarysystem.repository.UserRepository;
import com.librarysystem.service.AuthService;
import com.librarysystem.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ─── US-001 ───────────────────────────────────────────────────────────────

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (userRepository.existsByDocument(request.getDocumento())) {
            throw new RuntimeException("El documento ya está registrado");
        }

        Role rolLector = roleRepository.findByName(RoleEnum.LECTOR)
                .orElseThrow(() -> new RuntimeException("Rol LECTOR no encontrado en la BD"));

        String numeroCarnet = "LIB-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();

        User user = User.builder()
                .firstName(request.getNombre())
                .lastName(request.getApellido())
                .document(request.getDocumento())
                .email(request.getEmail())
                .phone(request.getTelefono())
                .address(request.getDireccion())
                .birthDate(request.getFechaNacimiento())
                .password(passwordEncoder.encode(request.getPassword()))
                .cardNumber(numeroCarnet)
                .active(true)
                .role(rolLector)
                .build();

        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        user.setRefreshToken(refreshToken);
        User savedUser = userRepository.save(user);

        String accessToken = jwtUtil.generateAccessToken(
                savedUser.getEmail(), RoleEnum.LECTOR.name(), savedUser.getId(), numeroCarnet);

        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .rol(RoleEnum.LECTOR.name())
                .numeroCarnet(numeroCarnet)
                .build();
    }

    // ─── US-002 ───────────────────────────────────────────────────────────────

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        if (!user.getActive()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario inactivo");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        String rol = user.getRole().getName().name();
        String numeroCarnet = rol.equals(RoleEnum.LECTOR.name()) ? user.getCardNumber() : null;

        String accessToken  = jwtUtil.generateAccessToken(user.getEmail(), rol, user.getId(), numeroCarnet);
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .rol(rol)
                .numeroCarnet(numeroCarnet)
                .build();
    }

    // ─── US-003 ───────────────────────────────────────────────────────────────

    @Override
    public AuthResponseDTO refreshToken(String refreshToken) {

        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido o expirado");
        }

        String email = jwtUtil.extractEmail(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido");
        }
        if (!user.getActive()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario inactivo");
        }

        String rol = user.getRole().getName().name();
        String numeroCarnet = rol.equals(RoleEnum.LECTOR.name()) ? user.getCardNumber() : null;

        String newAccessToken = jwtUtil.generateAccessToken(user.getEmail(), rol, user.getId(), numeroCarnet);

        return AuthResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .rol(rol)
                .numeroCarnet(numeroCarnet)
                .build();
    }
}