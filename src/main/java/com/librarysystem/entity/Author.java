package com.librarysystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "authors")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String apellido;
    private String nacionalidad;

    private LocalDate fechaNacimiento;
    private LocalDate fechaFallecimiento;

    @Column(columnDefinition = "TEXT")
    private String biografia;

    @Column(nullable = false)
    private Boolean activo = true;
}