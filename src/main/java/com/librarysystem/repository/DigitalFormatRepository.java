package com.librarysystem.repository;

import com.librarysystem.entity.DigitalFormat;
import com.librarysystem.entity.FormatoDigital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DigitalFormatRepository extends JpaRepository<DigitalFormat, Long> {

    List<DigitalFormat> findByBookIdAndActivoTrue(Long bookId);

    List<DigitalFormat> findByFormatoAndActivoTrue(FormatoDigital formato);
}