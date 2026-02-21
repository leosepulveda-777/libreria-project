package com.librarysystem.controller;

import com.librarysystem.dto.BookRequestDTO;
import com.librarysystem.dto.BookResponseDTO;
import com.librarysystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
// 1. Agrupamos este controlador bajo la etiqueta "Libros"
@Tag(name = "1. Libros", description = "Endpoints para la gestión completa del catálogo de libros")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo libro", description = "Registra un libro en la base de datos validando los campos obligatorios")
    @ApiResponse(responseCode = "201", description = "Libro creado exitosamente")
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookRequestDTO dto) {
        BookResponseDTO created = bookService.createBook(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todos los libros", description = "Obtiene una lista de todos los libros registrados")
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener libro por ID", description = "Busca un libro específico usando su identificador único")
    @ApiResponse(responseCode = "200", description = "Libro encontrado")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un libro", description = "Modifica los datos de un libro existente")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO dto) {
        return ResponseEntity.ok(bookService.updateBook(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un libro", description = "Borra permanentemente un libro de la base de datos")
    @ApiResponse(responseCode = "204", description = "Libro eliminado correctamente")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
