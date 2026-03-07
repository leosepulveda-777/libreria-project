package com.librarysystem.controller;

import com.librarysystem.dto.CopyRequestDTO;
import com.librarysystem.dto.CopyResponseDTO;
import com.librarysystem.service.CopyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/copies")
@RequiredArgsConstructor
@Tag(name = "7. Ejemplares", description = "Endpoints para la gestión de ejemplares físicos")
public class CopyController {

    private final CopyService copyService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Crear un nuevo ejemplar", description = "Registra un ejemplar físico en la base de datos")
    @ApiResponse(responseCode = "201", description = "Ejemplar creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    public ResponseEntity<CopyResponseDTO> createCopy(@Valid @RequestBody CopyRequestDTO request) {
        CopyResponseDTO created = copyService.createCopy(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Listar todos los ejemplares", description = "Obtiene una lista de todos los ejemplares registrados")
    public ResponseEntity<List<CopyResponseDTO>> getAllCopies() {
        return ResponseEntity.ok(copyService.getAllCopies());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Obtener ejemplar por ID", description = "Busca un ejemplar específico usando su identificador único")
    @ApiResponse(responseCode = "200", description = "Ejemplar encontrado")
    @ApiResponse(responseCode = "404", description = "Ejemplar no encontrado")
    public ResponseEntity<CopyResponseDTO> getCopyById(@PathVariable Long id) {
        return ResponseEntity.ok(copyService.getCopyById(id));
    }

    @GetMapping("/book/{bookId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Listar ejemplares de un libro", description = "Obtiene todos los ejemplares de un libro específico")
    public ResponseEntity<List<CopyResponseDTO>> getCopiesByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(copyService.getCopiesByBook(bookId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Actualizar un ejemplar", description = "Modifica los datos de un ejemplar existente")
    @ApiResponse(responseCode = "200", description = "Ejemplar actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Ejemplar no encontrado")
    public ResponseEntity<CopyResponseDTO> updateCopy(
            @PathVariable Long id,
            @Valid @RequestBody CopyRequestDTO request) {
        return ResponseEntity.ok(copyService.updateCopy(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Eliminar un ejemplar", description = "Borra permanentemente un ejemplar de la base de datos")
    @ApiResponse(responseCode = "204", description = "Ejemplar eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Ejemplar no encontrado")
    public ResponseEntity<Void> deleteCopy(@PathVariable Long id) {
        copyService.deleteCopy(id);
        return ResponseEntity.noContent().build();
    }
}