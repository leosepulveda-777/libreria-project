package com.librarysystem.service.impl;

import com.librarysystem.service.BookService;
import com.librarysystem.dto.BookRequestDTO;
import com.librarysystem.dto.BookResponseDTO;
import com.librarysystem.entity.Book;
import com.librarysystem.entity.Author;
import com.librarysystem.entity.Category;
import com.librarysystem.repository.BookRepository;
import com.librarysystem.repository.AuthorRepository;
import com.librarysystem.repository.CategoryRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public BookServiceImpl(BookRepository bookRepository,
                           AuthorRepository authorRepository,
                           CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
    }

    // ✅ CREATE
    @Override
    public BookResponseDTO createBook(BookRequestDTO dto) {

        List<Author> authors = authorRepository.findAllById(dto.getAuthorIds());

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Book book = Book.builder()
                .title(dto.getTitle())
                .isbn(dto.getIsbn())
                .publicationYear(dto.getPublicationYear())
                .synopsis(dto.getSynopsis())
                .editorial(dto.getEditorial())
                .imageUrl(dto.getImageUrl())
                .tipo(dto.getTipo())
                .authors(authors)
                .category(category)
                .active(true)
                .build();

        return mapToResponse(bookRepository.save(book));
    }

    // ✅ LIST
    @Override
    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ GET BY ID
    @Override
    public BookResponseDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        return mapToResponse(book);
    }

    // ✅ UPDATE
    @Override
    public BookResponseDTO updateBook(Long id, BookRequestDTO dto) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        List<Author> authors = authorRepository.findAllById(dto.getAuthorIds());

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublicationYear(dto.getPublicationYear());
        book.setSynopsis(dto.getSynopsis());
        book.setEditorial(dto.getEditorial());
        book.setImageUrl(dto.getImageUrl());
        book.setTipo(dto.getTipo());
        book.setAuthors(authors);
        book.setCategory(category);

        return mapToResponse(bookRepository.save(book));
    }

    //  DELETE (SOFT DELETE )
    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        book.setActive(false);
        bookRepository.save(book);
    }

    //  MAPPER
    private BookResponseDTO mapToResponse(Book book) {
        return BookResponseDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .publicationYear(book.getPublicationYear())
                .synopsis(book.getSynopsis())
                .active(book.getActive())
                .editorial(book.getEditorial())
                .imageUrl(book.getImageUrl())
                .tipo(book.getTipo())
                .authors(
                        book.getAuthors() != null
                                ? book.getAuthors()
                                .stream()
                                .map(a -> a.getNombre() + " " + a.getApellido())
                                .toList()
                                : List.of()
                )
                .category(
                        book.getCategory() != null
                                ? book.getCategory().getNombre()
                                : null
                )
                .build();
    }
}