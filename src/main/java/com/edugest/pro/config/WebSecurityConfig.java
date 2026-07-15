package com.edugest.pro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Définir explicitement la politique CORS en premier
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 2. Désactiver le CSRF (impératif pour Axios / REST JWT)
            .csrf(csrf -> csrf.disable())
            
            // 3. Configurer les autorisations de manière stricte
            .authorizeHttpRequests(auth -> auth
                // 1. Accès libre pour login/register
                .requestMatchers("/api/auth/**").permitAll() 
                
                // 2. Signalements (Accès libre / anonyme)
                .requestMatchers(HttpMethod.POST, "/api/signalements").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/signalements/**").permitAll()
                
                // 🎯 CORRECTION : Accès libre temporaire pour tester la saisie des notes sans blocage JWT
                .requestMatchers("/api/notes/**").permitAll()
                
                // 3. Tout le reste requiert d'être authentifié
                .anyRequest().authenticated() 
            )
            
            // 4. Rendre la session Stateless (Gestion par Token uniquement)
            .sessionManagement(session -> session
                .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
            );

        // 5. Placer ton filtre JWT juste avant le filtre d'authentification standard
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:5173", "http://localhost:4200"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "Cache-Control", "Pragma", "Expires"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); 
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}