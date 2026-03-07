package com.librarysystem.repository;

import com.librarysystem.entity.Copy;
import com.librarysystem.entity.EstadoEjemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CopyRepository extends JpaRepository<Copy, Long> {

    List<Copy> findByBookIdAndActiveTrue(Long bookId);

    List<Copy> findByStatusAndActiveTrue(EstadoEjemplar status);

    @Query("SELECT COUNT(c) FROM Copy c WHERE c.book.id = :bookId AND c.status = :status AND c.active = true")
    long countByBookIdAndStatusAndActiveTrue(@Param("bookId") Long bookId, @Param("status") EstadoEjemplar status);

    @Query("SELECT c FROM Copy c WHERE c.book.id = :bookId AND c.active = true ORDER BY c.copyNumber")
    List<Copy> findByBookIdOrderByCopyNumber(@Param("bookId") Long bookId);
}