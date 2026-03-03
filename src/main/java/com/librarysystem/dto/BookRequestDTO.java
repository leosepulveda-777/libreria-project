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

    @NotNull(message = "El año de publicación es obligatorio")
    @Min(1000)
    @Max(2100)
    private Integer publicationYear;

    @Size(max = 1000)
    private String synopsis;

    // NUEVOS
    private String editorial;

    private String imageUrl;

    @NotNull
    private TipoLibro tipo;

    // RELACIONES
    private List<Long> authorIds;

    private Long categoryId;
}