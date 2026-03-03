package com.librarysystem.repository;

import com.librarysystem.entity.Role;
import com.librarysystem.entity.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // Busca un rol por su nombre (ADMIN, BIBLIOTECARIO, LECTOR)
    // Lo usamos en AuthServiceImpl para asignar el rol LECTOR al registrarse
    Optional<Role> findByName(RoleEnum name);
}