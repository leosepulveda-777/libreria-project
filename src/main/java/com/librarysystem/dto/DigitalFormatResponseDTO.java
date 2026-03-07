package com.librarysystem.dto;

import com.librarysystem.entity.FormatoDigital;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalFormatResponseDTO {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private String urlDescarga;
    private FormatoDigital formato;
    private Double tamanoMb;
    private Boolean activo;
}