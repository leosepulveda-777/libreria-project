package com.librarysystem.repository;

import com.librarysystem.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Page<Author> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}