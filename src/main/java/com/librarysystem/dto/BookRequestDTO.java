package com.librarysystem.dto;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequestDTO {

    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotBlank(message = "El ISBN es obligatorio")
    private String isbn;

    @NotNull(message = "El año de publicación es obligatorio")
    @Min(value = 1000, message = "El año no es válido")
    @Max(value = 2100, message = "El año no es válido")
    private Integer publicationYear;

    @Size(max = 1000, message = "La sinopsis no puede superar los 1000 caracteres")
    private String synopsis;
}
