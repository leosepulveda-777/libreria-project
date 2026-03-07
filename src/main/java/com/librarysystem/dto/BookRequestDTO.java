package com.librarysystem.dto;

import com.librarysystem.entity.TipoLibro;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

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

    private String publisher;

    @NotNull(message = "El año de publicación es obligatorio")
    @Min(value = 1000, message = "El año no es válido")
    @Max(value = 2100, message = "El año no es válido")
    private Integer publicationYear;

    @Size(max = 1000, message = "La sinopsis no puede superar los 1000 caracteres")
    private String synopsis;

    private String imageUrl;

    @NotNull(message = "El tipo de libro es obligatorio")
    private TipoLibro tipo;

    private Long categoryId;

    @NotEmpty(message = "Debe especificar al menos un autor")
    private List<Long> authorIds;
}