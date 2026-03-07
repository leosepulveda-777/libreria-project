package com.librarysystem.controller;

import com.librarysystem.dto.AuthorRequest;
import com.librarysystem.dto.AuthorResponse;
import com.librarysystem.service.AuthorService;
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
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
@Tag(name = "5. Autores", description = "Endpoints para la gestión de autores")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Crear un nuevo autor", description = "Registra un autor en la base de datos")
    @ApiResponse(responseCode = "201", description = "Autor creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "409", description = "Ya existe un autor con ese nombre")
    public ResponseEntity<AuthorResponse> createAuthor(@Valid @RequestBody AuthorRequest request) {
        AuthorResponse created = authorService.createAuthor(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Listar todos los autores", description = "Obtiene una lista de todos los autores registrados")
    public ResponseEntity<List<AuthorResponse>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Obtener autor por ID", description = "Busca un autor específico usando su identificador único")
    @ApiResponse(responseCode = "200", description = "Autor encontrado")
    @ApiResponse(responseCode = "404", description = "Autor no encontrado")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Buscar autores", description = "Busca autores por nombre o nacionalidad")
    public ResponseEntity<List<AuthorResponse>> searchAuthors(@RequestParam String keyword) {
        return ResponseEntity.ok(authorService.searchAuthors(keyword));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Actualizar un autor", description = "Modifica los datos de un autor existente")
    @ApiResponse(responseCode = "200", description = "Autor actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Autor no encontrado")
    public ResponseEntity<AuthorResponse> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.ok(authorService.updateAuthor(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Eliminar un autor", description = "Borra permanentemente un autor de la base de datos")
    @ApiResponse(responseCode = "204", description = "Autor eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Autor no encontrado")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}