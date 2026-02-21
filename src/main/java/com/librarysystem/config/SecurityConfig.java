package com.librarysystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// @EnableMethodSecurity activa el uso de @PreAuthorize en los controllers
// Lo necesitamos para US-004 (solo ADMIN) y US-005 (ADMIN y BIBLIOTECARIO)
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Desactivamos CSRF porque usamos JWT (no cookies de sesión)
                .csrf(csrf -> csrf.disable())

                // Sin estado: cada request lleva su propio token, no hay sesión guardada en servidor
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        // Por ahora dejamos TODO permitido mientras desarrollamos US-001/002/003
                        // Cuando terminemos autenticación, aquí irán las reglas por rol
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    // BCrypt es el algoritmo que encripta los passwords antes de guardarlos en BD
    // Se usa en US-001 (registro) y US-002 (login para comparar el password)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager es el que valida usuario+password en el login
    // Spring lo necesita como Bean para inyectarlo en AuthServiceImpl
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}