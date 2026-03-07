package com.librarysystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "digital_formats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DigitalFormat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private String urlDescarga;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormatoDigital formato;

    @Column(nullable = false)
    private Double tamanoMb;

    @Column(nullable = false)
    private Boolean activo = true;
}
