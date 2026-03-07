package com.librarysystem.dto;

import com.librarysystem.entity.EstadoEjemplar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CopyResponseDTO {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private String copyNumber;
    private String barcode;
    private String location;
    private EstadoEjemplar status;
    private LocalDateTime acquisitionDate;
    private String notes;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}