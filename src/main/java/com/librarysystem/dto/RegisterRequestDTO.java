package com.librarysystem.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequestDTO {

    // @NotBlank = no puede ser null ni vacío ni solo espacios
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    // @NotBlank asegura que venga el documento
    @NotBlank(message = "El documento es obligatorio")
    private String documento;

    // @Email vavlida que tenga formato correcto (algo@algo.com)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    private String telefono;

    private String direccion;

    private LocalDate fechaNacimiento;

    // @Size asegura que el password tenga mínimo 6 caracteres
    @NotBlank(message = "El password es obligatorio")
    @Size(min = 6, message = "El password debe tener mínimo 6 caracteres")
    private String password;
}