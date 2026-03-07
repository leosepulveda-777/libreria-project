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
    private String publisher;
    private Integer publicationYear;
    private String synopsis;
    private String imageUrl;
    private TipoLibro tipo;
    private String category;
    private List<String> authors;
    private Integer ejemplaresTotales;
    private Integer ejemplaresDisponibles;
    private Boolean disponible;
    private Boolean active;
}