package com.edugest.pro.controllers;

import com.edugest.pro.models.Utilisateur; // Assurez-vous que votre entité s'appelle ainsi ou 'User'
import com.edugest.pro.repositories.UtilisateurRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;

    public AuthController(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> data) {
        // Vérification si l'email existe déjà
        if (utilisateurRepository.existsByEmail(data.get("email"))) {
            return ResponseEntity.badRequest().body(Map.of("message", "Erreur : Cet email est déjà utilisé !"));
        }

        // Création de l'utilisateur (Simplifié pour le test, sans BCrypt pour l'instant)
        Utilisateur user = new Utilisateur();
        user.setNom(data.get("nom"));
        user.setEmail(data.get("email"));
        user.setMotDePasse(data.get("motDePasse"));
        user.setRole(data.get("role"));

        utilisateurRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Utilisateur enregistré avec succès"));
    }
}