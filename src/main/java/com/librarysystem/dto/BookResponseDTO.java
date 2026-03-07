package com.librarysystem.dto;

import lombok.*;

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
}
