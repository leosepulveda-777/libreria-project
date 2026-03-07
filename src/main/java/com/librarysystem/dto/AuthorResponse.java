package com.librarysystem.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthorResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String nacionalidad;
}