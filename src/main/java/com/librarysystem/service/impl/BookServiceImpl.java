package com.librarysystem.service.impl;

import com.librarysystem.dto.*;
import com.librarysystem.entity.*;
import com.librarysystem.exception.ResourceNotFoundException;
import com.librarysystem.repository.*;
import com.librarysystem.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final CopyRepository copyRepository;
    private final DigitalFormatRepository digitalFormatRepository;

    @Override
    public BookResponseDTO createBook(BookRequestDTO dto) {
        if (bookRepository.findByIsbnAndActiveTrue(dto.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un libro con ese ISBN");
        }

        Book book = Book.builder()
                .title(dto.getTitle())
                .isbn(dto.getIsbn())
                .publicationYear(dto.getPublicationYear())
                .synopsis(dto.getSynopsis())
                .tipo(dto.getTipo())
                .active(true)
                .build();

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
            book.setCategory(category);
        }

        if (dto.getAuthorIds() != null && !dto.getAuthorIds().isEmpty()) {
            List<Author> authors = authorRepository.findAllById(dto.getAuthorIds());
            if (authors.size() != dto.getAuthorIds().size()) {
                throw new ResourceNotFoundException("Uno o más autores no encontrados");
            }
            book.setAuthors(authors);
        }

        Book saved = bookRepository.save(book);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findByActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));
        return mapToResponse(book);
    }

    @Override
    public BookResponseDTO updateBook(Long id, BookRequestDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublicationYear(dto.getPublicationYear());
        book.setSynopsis(dto.getSynopsis());
        book.setTipo(dto.getTipo());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
            book.setCategory(category);
        } else {
            book.setCategory(null);
        }

        if (dto.getAuthorIds() != null && !dto.getAuthorIds().isEmpty()) {
            List<Author> authors = authorRepository.findAllById(dto.getAuthorIds());
            if (authors.size() != dto.getAuthorIds().size()) {
                throw new ResourceNotFoundException("Uno o más autores no encontrados");
            }
            book.setAuthors(authors);
        } else {
            book.setAuthors(null);
        }

        return mapToResponse(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));
        book.setActive(false);
        bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> searchByKeyword(String keyword) {
        return bookRepository.searchByKeyword(keyword).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> searchByAuthor(String author) {
        return bookRepository.searchByAuthor(author).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> searchByCategory(Long categoryId) {
        return bookRepository.findByCategoryIdAndActiveTrue(categoryId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> searchByTipo(TipoLibro tipo) {
        return bookRepository.findByTipoAndActiveTrue(tipo).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> searchAdvanced(String keyword, Long categoryId, TipoLibro tipo) {
        return bookRepository.searchAdvanced(keyword, categoryId, tipo).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookDetailResponseDTO getBookDetail(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        return BookDetailResponseDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .publicationYear(book.getPublicationYear())
                .synopsis(book.getSynopsis())
                .tipo(book.getTipo())
                .category(book.getCategory() != null ? mapCategoryToResponse(book.getCategory()) : null)
                .authors(book.getAuthors() != null ?
                        book.getAuthors().stream()
                                .map(this::mapAuthorToResponse)
                                .collect(Collectors.toList()) : null)
                .ejemplares(book.getCopies() != null ?
                        book.getCopies().stream()
                                .filter(Copy::getActive)
                                .map(this::mapCopyToDetailResponse)
                                .collect(Collectors.toList()) : null)
                .formatosDigitales(book.getDigitalFormats() != null ?
                        book.getDigitalFormats().stream()
                                .filter(DigitalFormat::getActivo)
                                .map(this::mapDigitalFormatToResponse)
                                .collect(Collectors.toList()) : null)
                .usuarioTienePrestado(false)
                .usuarioTieneReservado(false)
                .active(book.getActive())
                .build();
    }

    @Override
    public DigitalFormatResponseDTO addDigitalFormat(Long bookId, DigitalFormatRequestDTO request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        DigitalFormat digitalFormat = DigitalFormat.builder()
                .book(book)
                .urlDescarga(request.getUrlDescarga())
                .formato(request.getFormato())
                .tamanoMb(request.getTamanoMb())
                .activo(true)
                .build();

        DigitalFormat saved = digitalFormatRepository.save(digitalFormat);
        return mapDigitalFormatToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DigitalFormatResponseDTO> getDigitalFormatsByBook(Long bookId) {
        return digitalFormatRepository.findByBookIdAndActivoTrue(bookId).stream()
                .map(this::mapDigitalFormatToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DigitalFormatResponseDTO updateDigitalFormat(Long id, DigitalFormatRequestDTO request) {
        DigitalFormat digitalFormat = digitalFormatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Formato digital no encontrado"));

        digitalFormat.setUrlDescarga(request.getUrlDescarga());
        digitalFormat.setFormato(request.getFormato());
        digitalFormat.setTamanoMb(request.getTamanoMb());

        DigitalFormat updated = digitalFormatRepository.save(digitalFormat);
        return mapDigitalFormatToResponse(updated);
    }

    @Override
    public void deleteDigitalFormat(Long id) {
        DigitalFormat digitalFormat = digitalFormatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Formato digital no encontrado"));
        digitalFormat.setActivo(false);
        digitalFormatRepository.save(digitalFormat);
    }

    private BookResponseDTO mapToResponse(Book book) {
        long totalCopies = copyRepository.countByBookIdAndStatusAndActiveTrue(book.getId(), EstadoEjemplar.DISPONIBLE) +
                copyRepository.countByBookIdAndStatusAndActiveTrue(book.getId(), EstadoEjemplar.PRESTADO) +
                copyRepository.countByBookIdAndStatusAndActiveTrue(book.getId(), EstadoEjemplar.RESERVADO);

        long availableCopies = copyRepository.countByBookIdAndStatusAndActiveTrue(book.getId(), EstadoEjemplar.DISPONIBLE);

        return BookResponseDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .publicationYear(book.getPublicationYear())
                .synopsis(book.getSynopsis())
                .tipo(book.getTipo())
                .category(book.getCategory() != null ? book.getCategory().getName() : null)
                .authors(book.getAuthors() != null ?
                        book.getAuthors().stream()
                                .map(Author::getName)
                                .collect(Collectors.toList()) : null)
                .ejemplaresTotales((int) totalCopies)
                .ejemplaresDisponibles((int) availableCopies)
                .disponible(availableCopies > 0)
                .active(book.getActive())
                .build();
    }

    private CategoryResponseDTO mapCategoryToResponse(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .nombre(category.getName())
                .descripcion(category.getDescription())
                .activa(category.getActive())
                .build();
    }

    private AuthorResponse mapAuthorToResponse(Author author) {
        return AuthorResponse.builder()
                .id(author.getId())
                .name(author.getName())
                .nationality(author.getNationality())
                .birthYear(author.getBirthYear())
                .biography(author.getBiography())
                .active(author.getActive())
                .build();
    }

    private CopyDetailResponseDTO mapCopyToDetailResponse(Copy copy) {
        return CopyDetailResponseDTO.builder()
                .id(copy.getId())
                .copyNumber(copy.getCopyNumber())
                .status(copy.getStatus())
                .acquisitionDate(copy.getAcquisitionDate())
                .notes(copy.getNotes())
                .active(copy.getActive())
                .build();
    }

    private DigitalFormatResponseDTO mapDigitalFormatToResponse(DigitalFormat digitalFormat) {
        return DigitalFormatResponseDTO.builder()
                .id(digitalFormat.getId())
                .bookId(digitalFormat.getBook().getId())
                .bookTitle(digitalFormat.getBook().getTitle())
                .urlDescarga(digitalFormat.getUrlDescarga())
                .formato(digitalFormat.getFormato())
                .tamanoMb(digitalFormat.getTamanoMb())
                .activo(digitalFormat.getActivo())
                .build();
    }
}