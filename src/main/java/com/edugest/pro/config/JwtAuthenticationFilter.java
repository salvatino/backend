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
        
        // Si la requête concerne l'authentification, on passe directement au filtre suivant
        if (requestURI.contains("/api/auth/")) {
            filterChain.doFilter(request, response);
            return; // On stoppe l'exécution ici pour cette route !
        }

        // ==========================================
        // LE RESTE DE TON CODE ACTUEL RESTE ICI :
        // (Extraction du Token, Validation, SecurityContextHolder...)
        // ==========================================
    }
}