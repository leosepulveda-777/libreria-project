package com.librarysystem.dto;

import com.librarysystem.entity.EstadoEjemplar;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "El libro es obligatorio")
    private Long bookId;

    @NotBlank(message = "El número de copia es obligatorio")
    private String copyNumber;

    @NotBlank(message = "El código de barras es obligatorio")
    private String barcode;

    @NotBlank(message = "La ubicación es obligatoria")
    private String location;

    @NotNull(message = "El estado es obligatorio")
    private EstadoEjemplar status;

    private LocalDateTime acquisitionDate;
    private String notes;
}