package com.librarysystem.service.impl;

import com.librarysystem.dto.CategoryRequestDTO;
import com.librarysystem.dto.CategoryResponseDTO;
import com.librarysystem.entity.Category;
import com.librarysystem.repository.CategoryRepository;
import com.librarysystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDTO crear(CategoryRequestDTO request) {

        if (categoryRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }

        Category categoriaPadre = null;
        if (request.getCategoriaPadreId() != null) {
            categoriaPadre = categoryRepository.findById(request.getCategoriaPadreId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Categoría padre no encontrada"));
        }

        Category category = Category.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activa(true)
                .categoriaPadre(categoriaPadre)
                .build();

        return toDTO(categoryRepository.save(category));
    }

    @Override
    public CategoryResponseDTO actualizar(Long id, CategoryRequestDTO request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        // Verificar nombre único solo si cambió
        if (!category.getNombre().equals(request.getNombre())
                && categoryRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }

        Category categoriaPadre = null;
        if (request.getCategoriaPadreId() != null) {
            if (request.getCategoriaPadreId().equals(id)) {
                throw new RuntimeException("Una categoría no puede ser su propio padre");
            }
            categoriaPadre = categoryRepository.findById(request.getCategoriaPadreId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Categoría padre no encontrada"));
        }

        category.setNombre(request.getNombre());
        category.setDescripcion(request.getDescripcion());
        category.setCategoriaPadre(categoriaPadre);

        return toDTO(categoryRepository.save(category));
    }

    @Override
    public CategoryResponseDTO obtenerPorId(Long id) {
        return toDTO(categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoría no encontrada")));
    }

    @Override
    public List<CategoryResponseDTO> listarTodas() {
        return categoryRepository.findByActivaTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponseDTO> listarRaices() {
        return categoryRepository.findByCategoriaPadreIsNullAndActivaTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public void desactivar(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoría no encontrada"));
        category.setActiva(false);
        categoryRepository.save(category);
    }

    private CategoryResponseDTO toDTO(Category c) {
        return CategoryResponseDTO.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .descripcion(c.getDescripcion())
                .activa(c.getActiva())
                .categoriaPadreId(c.getCategoriaPadre() != null ? c.getCategoriaPadre().getId() : null)
                .categoriaPadreNombre(c.getCategoriaPadre() != null ? c.getCategoriaPadre().getNombre() : null)
                .build();
    }
}