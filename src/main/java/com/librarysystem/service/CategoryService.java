package com.librarysystem.service;

import com.librarysystem.dto.CategoryRequestDTO;
import com.librarysystem.dto.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryRequestDTO request);

    CategoryResponseDTO getCategoryById(Long id);

    List<CategoryResponseDTO> getAllCategories();

    List<CategoryResponseDTO> getRootCategories();

    List<CategoryResponseDTO> searchCategories(String keyword);

    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request);

    void deleteCategory(Long id);
}