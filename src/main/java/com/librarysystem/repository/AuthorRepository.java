package com.librarysystem.repository;

import com.librarysystem.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByNameAndActiveTrue(String name);

    List<Author> findByActiveTrue();

    @Query("SELECT a FROM Author a WHERE a.active = true AND " +
           "(LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.nationality) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Author> searchByKeyword(@Param("keyword") String keyword);
}