package com.librarysystem.dto;

import com.librarysystem.entity.TipoLibro;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDTO {

    private Long id;
    private String title;
    private String isbn;
    private Integer publicationYear;
    private String synopsis;
    private Boolean active;

    //  NUEVOS
    private String editorial;
    private String imageUrl;
    private TipoLibro tipo;

    //  RELACIONES (solo info básica)
    private List<String> authors;
    private String category;
}