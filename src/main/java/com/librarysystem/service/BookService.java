package com.librarysystem.service;

import com.librarysystem.dto.*;
import com.librarysystem.entity.TipoLibro;

import java.util.List;

public interface BookService {
    BookResponseDTO createBook(BookRequestDTO dto);
    List<BookResponseDTO> getAllBooks();
    BookResponseDTO getBookById(Long id);
    BookResponseDTO updateBook(Long id, BookRequestDTO dto);
    void deleteBook(Long id);

    // Métodos de búsqueda para catálogo público
    List<BookResponseDTO> searchByKeyword(String keyword);
    List<BookResponseDTO> searchByAuthor(String author);
    List<BookResponseDTO> searchByCategory(Long categoryId);
    List<BookResponseDTO> searchByTipo(TipoLibro tipo);
    List<BookResponseDTO> searchAdvanced(String keyword, Long categoryId, TipoLibro tipo);

    // Método para detalle completo del libro
    BookDetailResponseDTO getBookDetail(Long id);

    // Gestión de formatos digitales
    DigitalFormatResponseDTO addDigitalFormat(Long bookId, DigitalFormatRequestDTO request);
    List<DigitalFormatResponseDTO> getDigitalFormatsByBook(Long bookId);
    DigitalFormatResponseDTO updateDigitalFormat(Long id, DigitalFormatRequestDTO request);
    void deleteDigitalFormat(Long id);
}
