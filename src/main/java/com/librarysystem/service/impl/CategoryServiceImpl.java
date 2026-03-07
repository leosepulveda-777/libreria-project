package com.librarysystem.service.impl;

import com.librarysystem.dto.CategoryRequestDTO;
import com.librarysystem.dto.CategoryResponseDTO;
import com.librarysystem.entity.Category;
import com.librarysystem.exception.ResourceNotFoundException;
import com.librarysystem.repository.CategoryRepository;
import com.librarysystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {
        if (categoryRepository.existsByName(request.getNombre())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una categoría con ese nombre");
        }

        Category category = Category.builder()
                .name(request.getNombre())
                .description(request.getDescripcion())
                .active(true)
                .build();

        if (request.getCategoriaPadreId() != null) {
            Category padre = categoryRepository.findById(request.getCategoriaPadreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría padre no encontrada"));
            category.setParentCategory(padre);
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
        return categoryRepository.findByActiveTrue().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getRootCategories() {
        return categoryRepository.findByParentCategoryIsNullAndActiveTrue().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> searchCategories(String keyword) {
        return categoryRepository.searchByKeyword(keyword).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        if (!category.getName().equals(request.getNombre())
                && categoryRepository.existsByName(request.getNombre())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una categoría con ese nombre");
        }

        category.setName(request.getNombre());
        category.setDescription(request.getDescripcion());

        if (request.getCategoriaPadreId() != null) {
            Category padre = categoryRepository.findById(request.getCategoriaPadreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría padre no encontrada"));
            category.setParentCategory(padre);
        } else {
            category.setParentCategory(null);
        }

        return toDTO(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        category.setActive(false);
        categoryRepository.save(category);
    }

    private CategoryResponseDTO toDTO(Category c) {
        return CategoryResponseDTO.builder()
                .id(c.getId())
                .nombre(c.getName())
                .descripcion(c.getDescription())
                .activa(c.getActive())
                .categoriaPadreId(c.getParentCategory() != null ? c.getParentCategory().getId() : null)
                .categoriaPadreNombre(c.getParentCategory() != null ? c.getParentCategory().getName() : null)
                .subcategorias(c.getSubcategories() != null
                        ? c.getSubcategories().stream()
                        .filter(sub -> Boolean.TRUE.equals(sub.getActive()))
                        .map(this::toDTO)
                        .collect(Collectors.toList())
                        : null)
                .build();
    }
}