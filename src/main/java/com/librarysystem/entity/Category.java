package com.librarysystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private String descripcion;

    @Column(nullable = false)
    private Boolean activa = true;

    // Relación jerárquica: una categoría puede tener una categoría padre
    @ManyToOne
    @JoinColumn(name = "categoria_padre_id")
    private Category categoriaPadre;
}