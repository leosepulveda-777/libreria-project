package com.librarysystem.service;

import com.librarysystem.dto.AuthorRequest;
import com.librarysystem.dto.AuthorResponse;

import java.util.List;

public interface AuthorService {

    AuthorResponse createAuthor(AuthorRequest request);

    AuthorResponse getAuthorById(Long id);

    List<AuthorResponse> getAllAuthors();

    List<AuthorResponse> searchAuthors(String keyword);

    AuthorResponse updateAuthor(Long id, AuthorRequest request);

    void deleteAuthor(Long id);
}