package com.edugest.pro.controllers;

import com.edugest.pro.models.Utilisateur;
import com.edugest.pro.repositories.UtilisateurRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthentificationController {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthentificationController(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Utilisateur utilisateur) {
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        return ResponseEntity.ok(utilisateurRepository.save(utilisateur));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        Optional<Utilisateur> user = utilisateurRepository.findByEmail(credentials.get("email"));
        if(user.isPresent() && passwordEncoder.matches(credentials.get("motDePasse"), user.get().getMotDePasse())) {
            // Retourne l'utilisateur et un jeton fictif pour le frontend
            return ResponseEntity.ok(Map.of("token", "fake-jwt-token", "user", user.get()));
        }
        return ResponseEntity.status(401).body("Identifiants incorrectes");
    }
}