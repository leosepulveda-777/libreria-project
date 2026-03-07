package com.librarysystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String document;

    @Column(unique = true)
    private String email;

    private String phone;
    private String address;
    private LocalDate birthDate;
    private String password;
    private String cardNumber;
    private Boolean active;
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}