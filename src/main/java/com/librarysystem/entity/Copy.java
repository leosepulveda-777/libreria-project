package com.librarysystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "copies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Copy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "copy_number", nullable = false)
    private String copyNumber;

    @Column(name = "barcode", unique = true)
    private String barcode;

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEjemplar status;

    @Column(name = "acquisition_date")
    private LocalDateTime acquisitionDate;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (acquisitionDate == null) {
            acquisitionDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}