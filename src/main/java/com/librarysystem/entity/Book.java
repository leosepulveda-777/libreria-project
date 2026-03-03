package com.librarysystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 YA TENÍAS
    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(length = 1000)
    private String synopsis;

    @Column(nullable = false)
    private Boolean active = true;


    private String editorial;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private TipoLibro tipo;

    // RELACIÓN MUCHOS A MUCHOS (AUTORES)
    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    // RELACIÓN CON CATEGORÍA
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}