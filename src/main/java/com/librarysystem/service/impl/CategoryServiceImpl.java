package com.librarysystem.service.impl;

import com.librarysystem.dto.CategoryRequestDTO;
import com.librarysystem.dto.CategoryResponseDTO;
import com.librarysystem.entity.Category;
import com.librarysystem.exception.ResourceNotFoundException;
import com.librarysystem.repository.CategoryRepository;
import com.librarysystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {
        if (categoryRepository.existsByNombre(request.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }

        Category category = Category.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activa(true)
                .build();

        if (request.getCategoriaPadreId() != null) {
            Category padre = categoryRepository.findById(request.getCategoriaPadreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría padre no encontrada"));
            category.setCategoriaPadre(padre);
        }

        return toDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryById(Long id) {
        return toDTO(categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findByActivaTrue().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getRootCategories() {
        return categoryRepository.findByCategoriaPadreIsNullAndActivaTrue().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> searchCategories(String keyword) {
        return categoryRepository.findByActivaTrue().stream()
                .filter(c -> c.getNombre().toLowerCase().contains(keyword.toLowerCase()))
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        if (!category.getNombre().equals(request.getNombre())
                && categoryRepository.existsByNombre(request.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }

        category.setNombre(request.getNombre());
        category.setDescripcion(request.getDescripcion());

        if (request.getCategoriaPadreId() != null) {
            Category padre = categoryRepository.findById(request.getCategoriaPadreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría padre no encontrada"));
            category.setCategoriaPadre(padre);
        } else {
            category.setCategoriaPadre(null);
        }

        return toDTO(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
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
                .subcategorias(c.getSubcategorias() != null
                        ? c.getSubcategorias().stream()
                        .filter(sub -> sub.getActiva())
                        .map(this::toDTO)
                        .collect(Collectors.toList())
                        : null)
                .build();
    }
}