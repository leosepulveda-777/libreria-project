package com.librarysystem.repository;

import com.librarysystem.entity.Book;
import com.librarysystem.entity.TipoLibro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbnAndActiveTrue(String isbn);

    List<Book> findByActiveTrue();

    List<Book> findByCategoryIdAndActiveTrue(Long categoryId);

    List<Book> findByTipoAndActiveTrue(TipoLibro tipo);

    @Query("SELECT b FROM Book b WHERE b.active = true AND " +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.synopsis) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Book> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT b FROM Book b JOIN b.authors a WHERE b.active = true AND " +
           "LOWER(a.name) LIKE LOWER(CONCAT('%', :autor, '%'))")
    List<Book> searchByAuthor(@Param("autor") String autor);





    @Query("SELECT b FROM Book b WHERE b.active = true AND " +
           "(:keyword IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.synopsis) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:categoryId IS NULL OR b.category.id = :categoryId) AND " +
           "(:tipo IS NULL OR b.tipo = :tipo)")
    List<Book> searchAdvanced(@Param("keyword") String keyword,
                             @Param("categoryId") Long categoryId,
                             @Param("tipo") TipoLibro tipo);
}
