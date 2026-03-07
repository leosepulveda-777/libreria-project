package com.librarysystem.controller;

import com.librarysystem.dto.*;
import com.librarysystem.entity.TipoLibro;
import com.librarysystem.service.BookService;
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
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "6. Libros", description = "Endpoints para la gestión completa del catálogo de libros")
public class BookController {

    private final BookService bookService;

    // ========== ENDPOINTS ADMINISTRATIVOS ==========

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Crear un nuevo libro", description = "Registra un libro en la base de datos validando los campos obligatorios")
    @ApiResponse(responseCode = "201", description = "Libro creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "409", description = "ISBN ya existe")
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookRequestDTO dto) {
        BookResponseDTO created = bookService.createBook(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Listar todos los libros", description = "Obtiene una lista de todos los libros registrados")
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Obtener libro por ID", description = "Busca un libro específico usando su identificador único")
    @ApiResponse(responseCode = "200", description = "Libro encontrado")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Actualizar un libro", description = "Modifica los datos de un libro existente")
    @ApiResponse(responseCode = "200", description = "Libro actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO dto) {
        return ResponseEntity.ok(bookService.updateBook(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Eliminar un libro", description = "Borra permanentemente un libro de la base de datos")
    @ApiResponse(responseCode = "204", description = "Libro eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // ========== ENDPOINTS DE CATÁLOGO PÚBLICO ==========

    @GetMapping("/catalog/search")
    @Operation(summary = "Buscar libros en catálogo", description = "Búsqueda pública de libros por palabra clave")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    public ResponseEntity<List<BookResponseDTO>> searchBooks(@RequestParam String keyword) {
        return ResponseEntity.ok(bookService.searchByKeyword(keyword));
    }

    @GetMapping("/catalog/search-author")
    @Operation(summary = "Buscar libros por autor", description = "Búsqueda pública de libros por nombre de autor")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    public ResponseEntity<List<BookResponseDTO>> searchBooksByAuthor(@RequestParam String author) {
        return ResponseEntity.ok(bookService.searchByAuthor(author));
    }

    @GetMapping("/catalog/search-category")
    @Operation(summary = "Buscar libros por categoría", description = "Búsqueda pública de libros por ID de categoría")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    public ResponseEntity<List<BookResponseDTO>> searchBooksByCategory(@RequestParam Long categoryId) {
        return ResponseEntity.ok(bookService.searchByCategory(categoryId));
    }

    @GetMapping("/catalog/search-tipo")
    @Operation(summary = "Buscar libros por tipo", description = "Búsqueda pública de libros por tipo (FISICO, DIGITAL, AMBOS)")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    public ResponseEntity<List<BookResponseDTO>> searchBooksByTipo(@RequestParam TipoLibro tipo) {
        return ResponseEntity.ok(bookService.searchByTipo(tipo));
    }

    @GetMapping("/catalog/search-advanced")
    @Operation(summary = "Búsqueda avanzada", description = "Búsqueda combinada por palabra clave, categoría y tipo")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    public ResponseEntity<List<BookResponseDTO>> searchBooksAdvanced(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) TipoLibro tipo) {
        return ResponseEntity.ok(bookService.searchAdvanced(keyword, categoryId, tipo));
    }

    @GetMapping("/catalog/{id}/detail")
    @Operation(summary = "Ver detalle completo de libro", description = "Obtiene información detallada de un libro incluyendo ejemplares y formatos digitales")
    @ApiResponse(responseCode = "200", description = "Detalle obtenido exitosamente")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    public ResponseEntity<BookDetailResponseDTO> getBookDetail(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookDetail(id));
    }

    // ========== ENDPOINTS DE FORMATOS DIGITALES ==========

    @PostMapping("/{bookId}/digital-formats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Agregar formato digital", description = "Agrega un formato digital (PDF/EPUB) a un libro")
    @ApiResponse(responseCode = "201", description = "Formato digital agregado exitosamente")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    public ResponseEntity<DigitalFormatResponseDTO> addDigitalFormat(
            @PathVariable Long bookId,
            @Valid @RequestBody DigitalFormatRequestDTO request) {
        DigitalFormatResponseDTO created = bookService.addDigitalFormat(bookId, request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{bookId}/digital-formats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Listar formatos digitales", description = "Obtiene todos los formatos digitales de un libro")
    @ApiResponse(responseCode = "200", description = "Formatos obtenidos exitosamente")
    public ResponseEntity<List<DigitalFormatResponseDTO>> getDigitalFormatsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getDigitalFormatsByBook(bookId));
    }

    @PutMapping("/digital-formats/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Actualizar formato digital", description = "Modifica un formato digital existente")
    @ApiResponse(responseCode = "200", description = "Formato digital actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Formato digital no encontrado")
    public ResponseEntity<DigitalFormatResponseDTO> updateDigitalFormat(
            @PathVariable Long id,
            @Valid @RequestBody DigitalFormatRequestDTO request) {
        return ResponseEntity.ok(bookService.updateDigitalFormat(id, request));
    }

    @DeleteMapping("/digital-formats/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BIBLIOTECARIO')")
    @Operation(summary = "Eliminar formato digital", description = "Borra un formato digital de un libro")
    @ApiResponse(responseCode = "204", description = "Formato digital eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Formato digital no encontrado")
    public ResponseEntity<Void> deleteDigitalFormat(@PathVariable Long id) {
        bookService.deleteDigitalFormat(id);
        return ResponseEntity.noContent().build();
    }
}
