package com.librarysystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Entity
@Table(name = "books")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String isbn; // US-006: ISBN único

    @Column(nullable = false)
    private String title;

    private String editorial;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(length = 1000)
    private String synopsis;

    private String imageUrl;

    // US-006: Tipo de libro (FISICO, DIGITAL, AMBOS)
    @Enumerated(EnumType.STRING)
    private BookType type;

    @Column(nullable = false)
    private Boolean active = true;

    // US-006: Un libro puede tener múltiples autores
    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    // US-007: Relación con ejemplares físicos (Ubicación, código barras)
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<PhysicalCopy> physicalCopies;

    // US-008: Relación con formatos digitales (URL, PDF/EPUB)
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<DigitalFormat> digitalFormats;
}
