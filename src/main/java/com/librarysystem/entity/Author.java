package com.librarysystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "authors")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // US-005: Create author (First name, Last name)
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    // US-005: Nationality and dates
    private String nationality;
    private LocalDate birthDate;
    private LocalDate deathDate;

    // US-005: Biography (long text)
    @Column(columnDefinition = "TEXT")
    private String biography;

    @Column(nullable = false)
    private Boolean active = true;
}
