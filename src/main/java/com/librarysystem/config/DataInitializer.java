package com.librarysystem.config;

import com.librarysystem.entity.Role;
import com.librarysystem.entity.RoleEnum;
import com.librarysystem.entity.User;
import com.librarysystem.repository.RoleRepository;
import com.librarysystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // 1. Crear roles si no existen
        for (RoleEnum rol : RoleEnum.values()) {
            if (roleRepository.findByName(rol).isEmpty()) {
                Role role = new Role();
                role.setName(rol);
                roleRepository.save(role);
            }
        }

        // 2. ADMIN
        if (!userRepository.existsByEmail("admin@biblioteca.com")) {
            User admin = User.builder()
                    .firstName("Admin")
                    .lastName("Sistema")
                    .document("000000001")
                    .email("admin@biblioteca.com")
                    .password(passwordEncoder.encode("admin123"))
                    .cardNumber("LIB-ADMIN001")
                    .active(true)
                    .role(roleRepository.findByName(RoleEnum.ADMIN).orElseThrow())
                    .build();
            userRepository.save(admin);
            System.out.println(" ADMIN creado: admin@biblioteca.com / admin123");
        }

        // 3. BIBLIOTECARIO
        if (!userRepository.existsByEmail("bibliotecario@biblioteca.com")) {
            User bibliotecario = User.builder()
                    .firstName("Carlos")
                    .lastName("Pérez")
                    .document("000000002")
                    .email("bibliotecario@biblioteca.com")
                    .password(passwordEncoder.encode("biblio123"))
                    .cardNumber("LIB-BIBLIO01")
                    .active(true)
                    .role(roleRepository.findByName(RoleEnum.BIBLIOTECARIO).orElseThrow())
                    .build();
            userRepository.save(bibliotecario);
            System.out.println(" BIBLIOTECARIO creado: bibliotecario@biblioteca.com / biblio123");
        }

        // 4. LECTOR (seed de prueba, además del registro normal)
        if (!userRepository.existsByEmail("lector@biblioteca.com")) {
            User lector = User.builder()
                    .firstName("María")
                    .lastName("González")
                    .document("000000003")
                    .email("lector@biblioteca.com")
                    .password(passwordEncoder.encode("lector123"))
                    .cardNumber("LIB-LECT001")
                    .active(true)
                    .role(roleRepository.findByName(RoleEnum.LECTOR).orElseThrow())
                    .build();
            userRepository.save(lector);
            System.out.println(" LECTOR creado: lector@biblioteca.com / lector123");
        }
    }
}