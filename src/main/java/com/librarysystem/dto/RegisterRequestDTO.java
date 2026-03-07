package com.librarysystem.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El documento es obligatorio")
    private String documento;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El password es obligatorio")
    @Size(min = 8, message = "El password debe tener mínimo 8 caracteres")  // FIX: era 6
    @Pattern(regexp = ".*[A-Z].*", message = "Debe tener al menos una mayúscula")
    @Pattern(regexp = ".*[a-z].*", message = "Debe tener al menos una minúscula")
    @Pattern(regexp = ".*\\d.*", message = "Debe tener al menos un número")
    private String password;
}