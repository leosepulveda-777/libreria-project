package com.librarysystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    // null = categoría raíz, con valor = subcategoría
    private Long categoriaPadreId;
}