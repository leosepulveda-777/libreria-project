package com.librarysystem.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean activa;
    private Long categoriaPadreId;
    private String categoriaPadreNombre;
    private List<CategoryResponseDTO> subcategorias;
}