package com.librarysystem.service;

import com.librarysystem.dto.BookRequestDTO;
import com.librarysystem.dto.BookResponseDTO;
import java.util.List;

public interface BookService {
    BookResponseDTO createBook(BookRequestDTO dto);
    List<BookResponseDTO> getAllBooks();
    BookResponseDTO getBookById(Long id);
    BookResponseDTO updateBook(Long id, BookRequestDTO dto);
    void deleteBook(Long id);
}
