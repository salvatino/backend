package com.edugest.pro.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        // 1. On laisse passer DIRECTEMENT les requêtes de vérification du navigateur (Preflight)
        if ("OPTIONS".equalsIgnoreCase(method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Si la route est l'authentification pure, pas besoin de chercher un token
        if (requestURI.contains("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // =========================================================================
        // 3. ICI SE TROUVE TON CODE JWT EXISTANT (NE PAS METTRE DE CONDITION SUR LE POST)
        // =========================================================================
        // String authHeader = request.getHeader("Authorization");
        // if (authHeader != null && authHeader.startsWith("Bearer ")) {
        //     String jwt = authHeader.substring(7);
        //     // ... Ta validation de token habituelle qui remplit le SecurityContextHolder
        // }
        // =========================================================================

        // 4. On laisse TOUJOURS la requête continuer vers la suite de la chaîne Spring Security
        filterChain.doFilter(request, response);
    }
}