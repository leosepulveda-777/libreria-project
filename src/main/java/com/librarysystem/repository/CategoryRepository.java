package com.librarysystem.repository;

import com.librarysystem.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNombre(String nombre);
    Optional<Category> findByNombre(String nombre);
    List<Category> findByActivaTrue();
    List<Category> findByCategoriaPadreIsNullAndActivaTrue(); // solo categorías raíz
}