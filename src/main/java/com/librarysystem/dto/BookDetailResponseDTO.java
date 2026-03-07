package com.librarysystem.dto;

import com.librarysystem.entity.TipoLibro;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDetailResponseDTO {
    private Long id;
    private String title;
    private String isbn;
    private Integer publicationYear;
    private String synopsis;
    private TipoLibro tipo;
    private CategoryResponseDTO category;
    private List<AuthorResponse> authors;
    private List<CopyDetailResponseDTO> ejemplares;
    private List<DigitalFormatResponseDTO> formatosDigitales;
    private Boolean usuarioTienePrestado;
    private Boolean usuarioTieneReservado;
    private Boolean active;
}