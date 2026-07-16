package com.edugest.pro.controllers;

import com.edugest.pro.config.JwtUtils; // 👈 Assurez-vous que l'import correspond au bon package
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
    private final JwtUtils jwtUtils; // 👈 1. Déclaration du composant manquante résolue

    // 👈 2. Injection via le constructeur existant pour un code propre et robuste
    public AuthController(UtilisateurRepository utilisateurRepository, JwtUtils jwtUtils) {
        this.utilisateurRepository = utilisateurRepository;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> data) {
        if (utilisateurRepository.existsByEmail(data.get("email"))) {
            return ResponseEntity.badRequest().body(Map.of("message", "Erreur : Cet email est déjà utilisé !"));
        }

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
        user.setMotDePasse(data.get("motDePasse")); 
        
        String roleStr = data.get("role");
        if (roleStr != null) {
            user.setRole(Role.valueOf(roleStr.toUpperCase().trim()));
        } else {
            user.setRole(Role.ELEVE);
        }

        utilisateurRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Utilisateur enregistré avec succès"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> data) {
        String email = data.get("email");
        String motDePasse = data.get("motDePasse") != null ? data.get("motDePasse") : data.get("password");

        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Erreur : Email ou mot de passe incorrect."));
        }

        Utilisateur user = userOpt.get();

        if (!user.getMotDePasse().equals(motDePasse)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Erreur : Email ou mot de passe incorrect."));
        }

        // 🛡️ 3. Utilisation sécurisée de jwtUtils (Ligne qui posait problème)
        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());

        return ResponseEntity.ok(Map.of(
            "id", user.getId(),
            "token", token,
            "email", user.getEmail(),
            "role", user.getRole().name(),
            "nom", user.getNom(),
            "prenom", user.getPrenom()
        ));
    }
}