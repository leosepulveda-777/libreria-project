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
public class CopyRequestDTO {
    private Long bookId;
    private String copyNumber;
    private EstadoEjemplar status;
    private LocalDateTime acquisitionDate;
    private String notes;
}