package com.librarysystem.service;

import com.librarysystem.dto.CopyRequestDTO;
import com.librarysystem.dto.CopyResponseDTO;

import java.util.List;

public interface CopyService {

    CopyResponseDTO createCopy(CopyRequestDTO request);

    CopyResponseDTO getCopyById(Long id);

    List<CopyResponseDTO> getCopiesByBook(Long bookId);

    List<CopyResponseDTO> getAllCopies();

    CopyResponseDTO updateCopy(Long id, CopyRequestDTO request);

    void deleteCopy(Long id);
}