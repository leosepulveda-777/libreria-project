package com.librarysystem.service;

import com.librarysystem.dto.CategoryRequestDTO;
import com.librarysystem.dto.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO crear(CategoryRequestDTO request);
    CategoryResponseDTO actualizar(Long id, CategoryRequestDTO request);
    CategoryResponseDTO obtenerPorId(Long id);
    List<CategoryResponseDTO> listarTodas();       // lista plana
    List<CategoryResponseDTO> listarRaices();      // solo categorías sin padre
    void desactivar(Long id);
}