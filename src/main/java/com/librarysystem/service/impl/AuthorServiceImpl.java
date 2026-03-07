package com.librarysystem.service.impl;

import com.librarysystem.dto.AuthorRequest;
import com.librarysystem.dto.AuthorResponse;
import com.librarysystem.entity.Author;
import com.librarysystem.exception.ResourceNotFoundException;
import com.librarysystem.repository.AuthorRepository;
import com.librarysystem.service.AuthorService;
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
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public AuthorResponse createAuthor(AuthorRequest request) {
        authorRepository.findByNameAndActiveTrue(request.getName()).ifPresent(a -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un autor con ese nombre");
        });

        Author author = Author.builder()
                .name(request.getName())
                .nationality(request.getNationality())
                .birthYear(request.getBirthYear())
                .biography(request.getBiography())
                .active(true)
                .build();

        return toResponse(authorRepository.save(author));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponse getAuthorById(Long id) {
        return toResponse(authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor no encontrado")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponse> getAllAuthors() {
        return authorRepository.findByActiveTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponse> searchAuthors(String keyword) {
        return authorRepository.searchByKeyword(keyword).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorResponse updateAuthor(Long id, AuthorRequest request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor no encontrado"));

        if (!author.getName().equals(request.getName())) {
            authorRepository.findByNameAndActiveTrue(request.getName()).ifPresent(a -> {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Ya existe un autor con ese nombre");
            });
        }

        author.setName(request.getName());
        author.setNationality(request.getNationality());
        author.setBirthYear(request.getBirthYear());
        author.setBiography(request.getBiography());

        return toResponse(authorRepository.save(author));
    }

    @Override
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor no encontrado"));
        author.setActive(false);
        authorRepository.save(author);
    }

    private AuthorResponse toResponse(Author author) {
        return AuthorResponse.builder()
                .id(author.getId())
                .name(author.getName())
                .nationality(author.getNationality())
                .birthYear(author.getBirthYear())
                .biography(author.getBiography())
                .active(author.getActive())
                .build();
    }
}