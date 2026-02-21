package com.librarysystem.security;

import com.librarysystem.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    // JwtUtil lo usamos para validar y leer el token
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Leer el header Authorization de la request
        // Debe venir así: "Bearer eyJhbGci..."
        String authHeader = request.getHeader("Authorization");

        // 2. Si no viene el header o no empieza con "Bearer ", dejamos pasar sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraemos solo el token (quitamos el "Bearer " del inicio)
        String token = authHeader.substring(7);

        // 4. Validamos el token
        if (!jwtUtil.isTokenValid(token)) {
            // Token inválido o vencido: dejamos pasar pero sin autenticar
            // Spring Security bloqueará si el endpoint requiere autenticación
            filterChain.doFilter(request, response);
            return;
        }

        // 5. Extraemos la info del token
        String email = jwtUtil.extractEmail(token);
        String rol = jwtUtil.extractRol(token);

        // 6. Creamos el objeto de autenticación con el rol del usuario
        // "ROLE_" es el prefijo que Spring Security espera para los roles
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + rol))
                );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        // 7. Guardamos la autenticación en el contexto de Spring Security
        // A partir de aquí Spring sabe quién es el usuario en esta request
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 8. Continuamos con el siguiente filtro
        filterChain.doFilter(request, response);
    }
}