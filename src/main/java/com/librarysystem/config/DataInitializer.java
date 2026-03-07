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
    public void run(String... args) {
        createRoleIfNotExists(RoleEnum.ADMIN.name(), "Administrador del sistema");
        createRoleIfNotExists(RoleEnum.BIBLIOTECARIO.name(), "Bibliotecario del sistema");
        createRoleIfNotExists(RoleEnum.LECTOR.name(), "Usuario lector");
        System.out.println("Roles inicializados correctamente");
    }

    private void createRoleIfNotExists(String name, String description) {
        if (roleRepository.findByName(name).isEmpty()) {
            roleRepository.save(Role.builder()
                    .name(name)
                    .description(description)
                    .build());
        }
    }
}