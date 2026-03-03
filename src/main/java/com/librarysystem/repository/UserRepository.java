package com.librarysystem.repository;

import com.librarysystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByDocument(String document);

    Optional<User> findByCardNumber(String cardNumber);

    boolean existsByEmail(String email);

    boolean existsByDocument(String document);
}
