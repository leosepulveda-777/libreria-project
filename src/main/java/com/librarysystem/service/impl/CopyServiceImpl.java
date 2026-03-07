package com.librarysystem.service.impl;

import com.librarysystem.dto.CopyRequestDTO;
import com.librarysystem.dto.CopyResponseDTO;
import com.librarysystem.entity.Book;
import com.librarysystem.entity.Copy;
import com.librarysystem.exception.ResourceNotFoundException;
import com.librarysystem.repository.BookRepository;
import com.librarysystem.repository.CopyRepository;
import com.librarysystem.service.CopyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CopyServiceImpl implements CopyService {

    private final CopyRepository copyRepository;
    private final BookRepository bookRepository;

    @Override
    public CopyResponseDTO createCopy(CopyRequestDTO request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado"));

        Copy copy = Copy.builder()
                .book(book)
                .copyNumber(request.getCopyNumber())
                .status(request.getStatus())
                .acquisitionDate(request.getAcquisitionDate())
                .notes(request.getNotes())
                .active(true)
                .build();

        Copy savedCopy = copyRepository.save(copy);
        return mapToResponse(savedCopy);
    }

    @Override
    @Transactional(readOnly = true)
    public CopyResponseDTO getCopyById(Long id) {
        Copy copy = copyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejemplar no encontrado"));
        return mapToResponse(copy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CopyResponseDTO> getCopiesByBook(Long bookId) {
        return copyRepository.findByBookIdAndActiveTrue(bookId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CopyResponseDTO> getAllCopies() {
        return copyRepository.findAll().stream()
                .filter(Copy::getActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CopyResponseDTO updateCopy(Long id, CopyRequestDTO request) {
        Copy copy = copyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejemplar no encontrado"));

        copy.setCopyNumber(request.getCopyNumber());
        copy.setStatus(request.getStatus());
        copy.setAcquisitionDate(request.getAcquisitionDate());
        copy.setNotes(request.getNotes());

        Copy updatedCopy = copyRepository.save(copy);
        return mapToResponse(updatedCopy);
    }

    @Override
    public void deleteCopy(Long id) {
        Copy copy = copyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejemplar no encontrado"));
        copy.setActive(false);
        copyRepository.save(copy);
    }

    private CopyResponseDTO mapToResponse(Copy copy) {
        return CopyResponseDTO.builder()
                .id(copy.getId())
                .bookId(copy.getBook().getId())
                .bookTitle(copy.getBook().getTitle())
                .copyNumber(copy.getCopyNumber())
                .status(copy.getStatus())
                .acquisitionDate(copy.getAcquisitionDate())
                .notes(copy.getNotes())
                .active(copy.getActive())
                .createdAt(copy.getCreatedAt())
                .updatedAt(copy.getUpdatedAt())
                .build();
    }
}