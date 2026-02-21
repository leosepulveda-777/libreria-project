package com.librarysystem.service.impl;

import com.librarysystem.dto.AuthResponseDTO;
import com.librarysystem.dto.RegisterRequestDTO;
import com.librarysystem.entity.Role;
import com.librarysystem.entity.RoleEnum;
import com.librarysystem.entity.User;
import com.librarysystem.repository.RoleRepository;
import com.librarysystem.repository.UserRepository;
import com.librarysystem.service.AuthService;
import com.librarysystem.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        // 1. Verificar que el email no esté registrado
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // 2. Verificar que el documento no esté registrado
        if (userRepository.existsByDocument(request.getDocumento())) {
            throw new RuntimeException("El documento ya está registrado");
        }

        // 3. Buscar el rol LECTOR en la BD
        Role rolLector = roleRepository.findByName(RoleEnum.LECTOR)
                .orElseThrow(() -> new RuntimeException("Rol LECTOR no encontrado. Verifica que esté en la BD"));

        // 4. Generar número de carnet único
        // Tomamos los primeros 8 caracteres de un UUID sin guiones
        String numeroCarnet = "LIB-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();

        // 5. Construir el usuario
        User user = User.builder()
                .firstName(request.getNombre())
                .lastName(request.getApellido())
                .document(request.getDocumento())
                .email(request.getEmail())
                .phone(request.getTelefono())
                .address(request.getDireccion())
                .birthDate(request.getFechaNacimiento())
                // Encriptamos el password con BCrypt antes de guardar
                .password(passwordEncoder.encode(request.getPassword()))
                .cardNumber(numeroCarnet)
                .active(true)
                .role(rolLector)
                .build();

        // 6. Generar tokens
        String accessToken = jwtUtil.generateAccessToken(
                user.getEmail(),
                RoleEnum.LECTOR.name(),
                null, // aún no tiene id porque no se ha guardado
                numeroCarnet
        );
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        // 7. Guardar refresh token en el usuario y persistir en BD
        user.setRefreshToken(refreshToken);
        User savedUser = userRepository.save(user);

        // 8. Regenerar access token con el id real asignado por la BD
        String finalAccessToken = jwtUtil.generateAccessToken(
                savedUser.getEmail(),
                RoleEnum.LECTOR.name(),
                savedUser.getId(),
                numeroCarnet
        );

        // 9. Retornar respuesta con tokens e info del usuario
        return AuthResponseDTO.builder()
                .accessToken(finalAccessToken)
                .refreshToken(refreshToken)
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .rol(RoleEnum.LECTOR.name())
                .numeroCarnet(numeroCarnet)
                .build();
    }
}