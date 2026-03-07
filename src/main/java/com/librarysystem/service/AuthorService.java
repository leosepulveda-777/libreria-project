package com.librarysystem.service;

import com.librarysystem.dto.*;
import com.librarysystem.entity.Author;
import com.librarysystem.repository.AuthorRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository repository;

    public AuthorResponse create(AuthorRequest request) {

        Author author = Author.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .nacionalidad(request.getNacionalidad())
                .fechaNacimiento(request.getFechaNacimiento())
                .fechaFallecimiento(request.getFechaFallecimiento())
                .biografia(request.getBiografia())
                .activo(true)
                .build();

        repository.save(author);

        return map(author);
    }

    public Page<AuthorResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::map);
    }

    public Page<AuthorResponse> search(String nombre, Pageable pageable) {
        return repository.findByNombreContainingIgnoreCase(nombre, pageable)
                .map(this::map);
    }

    public AuthorResponse update(Long id, AuthorRequest request) {

        Author author = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));

        author.setNombre(request.getNombre());
        author.setApellido(request.getApellido());
        author.setNacionalidad(request.getNacionalidad());
        author.setFechaNacimiento(request.getFechaNacimiento());
        author.setFechaFallecimiento(request.getFechaFallecimiento());
        author.setBiografia(request.getBiografia());

        repository.save(author);

        return map(author);
    }

    private AuthorResponse map(Author a) {
        return AuthorResponse.builder()
                .id(a.getId())
                .nombre(a.getNombre())
                .apellido(a.getApellido())
                .nacionalidad(a.getNacionalidad())
                .build();
    }
}