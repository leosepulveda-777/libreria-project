package com.librarysystem.controller;

import com.librarysystem.dto.CategoryRequestDTO;
import com.librarysystem.dto.CategoryResponseDTO;
import com.librarysystem.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "US-004 – Crear categoría")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> crear(@Valid @RequestBody CategoryRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.crear(request));
    }

    @Operation(summary = "US-004 – Listar todas las categorías activas")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> listarTodas() {
        return ResponseEntity.ok(categoryService.listarTodas());
    }

    @Operation(summary = "US-004 – Listar categorías raíz (sin padre)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/raices")
    public ResponseEntity<List<CategoryResponseDTO>> listarRaices() {
        return ResponseEntity.ok(categoryService.listarRaices());
    }

    @Operation(summary = "US-004 – Obtener categoría por ID")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.obtenerPorId(id));
    }

    @Operation(summary = "US-004 – Actualizar categoría")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody CategoryRequestDTO request) {
        return ResponseEntity.ok(categoryService.actualizar(id, request));
    }

    @Operation(summary = "US-004 – Desactivar categoría")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        categoryService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}