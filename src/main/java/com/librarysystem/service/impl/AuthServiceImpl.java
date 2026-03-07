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

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }
        if (userRepository.existsByDocument(request.getDocumento())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El documento ya está registrado");
        }

        Role rolLector = roleRepository.findByName(RoleEnum.LECTOR.name())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Rol LECTOR no encontrado en la BD"));

        String numeroCarnet = "LIB-" + UUID.randomUUID()
                .toString().replace("-", "").substring(0, 8).toUpperCase();

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
                savedUser.getEmail(), RoleEnum.LECTOR.name(),
                savedUser.getId(), numeroCarnet);

        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .rol(RoleEnum.LECTOR.name())
                .numeroCarnet(numeroCarnet)
                .build();
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario inactivo");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        String accessToken = jwtUtil.generateAccessToken(
                user.getEmail(), user.getRole().getName(),
                user.getId(), user.getCardNumber());

        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .rol(user.getRole().getName())
                .numeroCarnet(user.getCardNumber())
                .build();
    }

    @Override
    public AuthResponseDTO refreshToken(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Refresh token inválido o expirado"));

        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expirado");
        }

        String newAccessToken = jwtUtil.generateAccessToken(
                user.getEmail(), user.getRole().getName(),
                user.getId(), user.getCardNumber());

        return AuthResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(user.getRefreshToken())
                .userId(user.getId())
                .email(user.getEmail())
                .rol(user.getRole().getName())
                .numeroCarnet(user.getCardNumber())
                .build();
    }
}