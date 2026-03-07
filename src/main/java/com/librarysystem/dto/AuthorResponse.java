package com.librarysystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponse {
    private Long id;
    private String name;
    private String lastName;
    private String fullName;
    private String nationality;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String biography;
    private Boolean active;
}