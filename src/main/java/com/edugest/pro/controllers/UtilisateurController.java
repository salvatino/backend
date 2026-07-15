package com.edugest.pro.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // Si tu utilises Spring Security
import com.edugest.pro.models.Utilisateur; // Adapte selon le package exact de ton entité
import com.edugest.pro.repositories.UtilisateurRepository;

@RestController
@RequestMapping("/api/utilisateurs")
@CrossOrigin(origins = "http://localhost:3000") // Permet à React de communiquer
public class UtilisateurController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Si tu utilises Spring Security

    @PostMapping("/inscription")
    public ResponseEntity<?> inscrireUtilisateur(@RequestBody Utilisateur utilisateur) {
        // 1. Vérifier si l'email existe déjà
        if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
            return ResponseEntity.badRequest().body("Erreur : Cet email est déjà utilisé.");
        }

        // 2. Hacher le mot de passe par défaut avant sauvegarde
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));

        // 3. Sauvegarder dans la base de données
        Utilisateur nouvelUtilisateur = utilisateurRepository.save(utilisateur);

        return ResponseEntity.ok(nouvelUtilisateur);
    }
}