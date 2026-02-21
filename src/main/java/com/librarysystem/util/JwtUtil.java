package com.librarysystem.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // Toma el valor de jwt.secret del application.properties
    @Value("${jwt.secret}")
    private String secret;

    // Toma el valor de jwt.expiration del application.properties (24 horas)
    @Value("${jwt.expiration}")
    private Long expiration;

    // Toma el valor de jwt.refresh-expiration del application.properties (7 días)
    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    // Convierte el secret string en una clave criptográfica real que entiende JJWT
    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // GENERAR ACCESS TOKEN
    // Recibe los datos del usuario y los mete dentro del token
    // claims = información extra que viaja dentro del token (rol, carnet, etc.)
    public String generateAccessToken(String email, String rol, Long userId, String numeroCarnet) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol);
        claims.put("userId", userId);
        // numeroCarnet solo va en el token si el usuario es LECTOR
        if (numeroCarnet != null) {
            claims.put("numeroCarnet", numeroCarnet);
        }
        return buildToken(claims, email, expiration);
    }

    //  GENERAR REFRESH TOKEN
    // El refresh token solo lleva el email, no necesita más info
    public String generateRefreshToken(String email) {
        return buildToken(new HashMap<>(), email, refreshExpiration);
    }

    // CONSTRUIR EL TOKEN
    // Método interno que arma el token con los claims, fechas y firma
    private String buildToken(Map<String, Object> claims, String subject, Long expirationTime) {
        return Jwts.builder()
                .claims(claims)
                // subject = el email del usuario (identificador principal)
                .subject(subject)
                // fecha de creación del token
                .issuedAt(new Date(System.currentTimeMillis()))
                // fecha de vencimiento = ahora + tiempo configurado
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                // firmamos el token con nuestra clave secreta
                .signWith(getSigningKey())
                .compact();
    }

    // EXTRAER EMAIL
    // Lee el email (subject) que está dentro del token
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    //  EXTRAER ROL
    public String extractRol(String token) {
        return extractAllClaims(token).get("rol", String.class);
    }

    //  EXTRAER USER ID
    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    // VALIDAR TOKEN
    // Retorna true si el token es válido y no ha vencido
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            // Si el token está malformado, falsificado o vencido, cae aquí
            return false;
        }
    }

    //  VERIFICAR VENCIMIENTO
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    //  EXTRAER TODOS LOS CLAIMS
    // Descifra y verifica la firma del token, retorna todo su contenido
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}