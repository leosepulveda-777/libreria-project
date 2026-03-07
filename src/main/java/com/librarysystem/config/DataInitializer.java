package com.librarysystem.config;

import com.librarysystem.entity.Role;
import com.librarysystem.entity.RoleEnum;
import com.librarysystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Role adminRole = Role.builder()
                    .name("ADMIN")
                    .description("Administrador del sistema")
                    .build();
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("BIBLIOTECARIO").isEmpty()) {
            Role bibliotecarioRole = Role.builder()
                    .name("BIBLIOTECARIO")
                    .description("Bibliotecario del sistema")
                    .build();
            roleRepository.save(bibliotecarioRole);
        }

        if (roleRepository.findByName("LECTOR").isEmpty()) {
            Role lectorRole = Role.builder()
                    .name("LECTOR")
                    .description("Usuario lector")
                    .build();
            roleRepository.save(lectorRole);
        }

        System.out.println("✅ Roles inicializados correctamente");
    }
}