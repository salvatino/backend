package com.edugest.pro.controllers;

import com.edugest.pro.models.enums.Role;
import com.edugest.pro.models.Utilisateur;
import com.edugest.pro.repositories.UtilisateurRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

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

        // Création de l'utilisateur
        Utilisateur user = new Utilisateur();
        
        String nomComplet = data.get("nomComplet") != null ? data.get("nomComplet") : data.get("nom");
        String nom = "";
        String prenom = "";

        if (nomComplet != null && !nomComplet.trim().isEmpty()) {
            String[] parties = nomComplet.trim().split(" ", 2);
            nom = parties[0].toUpperCase();
            if (parties.length > 1) {
                prenom = parties[1];
            }
        }

        user.setNom(nom);
        user.setPrenom(prenom);
        user.setEmail(data.get("email"));
        user.setMotDePasse(data.get("motDePasse")); // En clair pour l'instant
        
        String roleStr = data.get("role");
        if (roleStr != null) {
            user.setRole(Role.valueOf(roleStr.toUpperCase().trim()));
        } else {
            user.setRole(Role.ELEVE);
        }

        utilisateurRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Utilisateur enregistré avec succès"));
    }

    // ==========================================
    // AJOUT DE LA MÉTHODE LOGIN MANQUANTE
    // ==========================================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> data) {
        String email = data.get("email");
        String motDePasse = data.get("motDePasse") != null ? data.get("motDePasse") : data.get("password");

        // 1. Chercher l'utilisateur par son email en BDD
        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Erreur : Email ou mot de passe incorrect."));
        }

        Utilisateur user = userOpt.get();

        // 2. Vérification du mot de passe en texte clair
        if (!user.getMotDePasse().equals(motDePasse)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Erreur : Email ou mot de passe incorrect."));
        }

        // 3. Connexion réussie ! On renvoie les infos de l'utilisateur et un jeton fictif
        // (Ajuste les clés JSON si ton React attend d'autres noms, ex: accessToken)
        return ResponseEntity.ok(Map.of(
            "id", user.getId(),
            "token", "fictif-jwt-token-pour-soutenance",
            "email", user.getEmail(),
            "role", user.getRole().name(),
            "nom", user.getNom(),
            "prenom", user.getPrenom()
        ));
    }
}