package com.edugest.pro.controllers;

import com.edugest.pro.models.Utilisateur;
import com.edugest.pro.models.Signalement;
import com.edugest.pro.models.Absence;
import com.edugest.pro.models.enums.Role;
import com.edugest.pro.models.enums.StatutSignalement;
import com.edugest.pro.repositories.UtilisateurRepository;
import com.edugest.pro.repositories.SignalementRepository;
import com.edugest.pro.repositories.AbsenceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*") // À affiner selon le port de votre application React
public class AdminController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private SignalementRepository signalementRepository;

    @Autowired
    private AbsenceRepository absenceRepository;

    // ==========================================
    // 📊 GRAPHES & STATISTIQUES GLOBALES
    // ==========================================
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalEleves = utilisateurRepository.findByRole(Role.ELEVE).size(); // S'adapte si vous utilisez Role.ELEVE ou Role.ROLE_ELEVE selon votre Enum
        long totalEnseignants = utilisateurRepository.findByRole(Role.ENSEIGNANT).size();
        
        List<Signalement> signalementsActifs = signalementRepository.findAllByOrderByDateSignalementDesc().stream()
                .filter(s -> s.getStatut() == StatutSignalement.NON_LU)
                .collect(Collectors.toList());

        List<Map<String, Object>> casCritiques = obtenirCasCritiquesAbsences();

        stats.put("totalEleves", totalEleves);
        stats.put("totalEnseignants", totalEnseignants);
        stats.put("totalSignalementsActifs", signalementsActifs.size());
        stats.put("totalCasCritiques", casCritiques.size());
        
        return ResponseEntity.ok(stats);
    }

    // ==========================================
    // 🛡️ SÉCURITÉ VBG & SIGNALEMENTS
    // ==========================================
    @GetMapping("/signalements")
    public List<Signalement> getAllSignalements() {
        return signalementRepository.findAllByOrderByDateSignalementDesc();
    }

    @PutMapping("/signalements/{id}/traiter")
    public ResponseEntity<?> traiterSignalement(@PathVariable Long id) {
        return signalementRepository.findById(id).map(sig -> {
            sig.setStatut(StatutSignalement.RESOLU); // Ajustez selon votre Enum (ex: StatutSignalement.TRAITE ou LU)
            signalementRepository.save(sig);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Le signalement a été traité avec succès.");
            return ResponseEntity.ok().body(response);
        }).orElse(ResponseEntity.notFound().build());
    }

    // ==========================================
    // 📉 SUIVI ACADÉMIQUE : ABSENCES (SEUIL 30H)
    // ==========================================
    @GetMapping("/absences/seuils-critiques")
    public ResponseEntity<List<Map<String, Object>>> getSeuilsCritiques() {
        return ResponseEntity.ok(obtenirCasCritiquesAbsences());
    }

    // ==========================================
    // 🎓 UTILISATEURS (COMPTES, INscriptions)
    // ==========================================
    @GetMapping("/enseignants")
    public List<Utilisateur> getEnseignants() {
        return utilisateurRepository.findByRole(Role.ENSEIGNANT);
    }

    @PostMapping("/utilisateurs")
    public ResponseEntity<Utilisateur> creerUtilisateur(@RequestBody Utilisateur utilisateur) {
        if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        // Sauvegarde brute de l'utilisateur (le chiffrement du mot de passe peut être géré ici ou via un service)
        Utilisateur savedUser = utilisateurRepository.save(utilisateur);
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/utilisateurs/{id}")
    public ResponseEntity<?> supprimerUtilisateur(@PathVariable Long id) {
        utilisateurRepository.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Utilisateur radié des registres MySQL.");
        return ResponseEntity.ok().body(response);
    }

    // --- ALGORITHME DE CALCUL INTERNE POUR LE SEUIL DES 30 HEURES ---
    private List<Map<String, Object>> obtenirCasCritiquesAbsences() {
        List<Absence> toutesLesAbsences = absenceRepository.findAll();
        
        // Groupement des heures non justifiées par Élève
        Map<Utilisateur, Integer> cumulAbsencesParEleve = new HashMap<>();
        for (Absence abs : toutesLesAbsences) {
            if (abs.getEleve() != null) {
                int heuresNJ = abs.getHeuresNonJustifiees();
                cumulAbsencesParEleve.put(abs.getEleve(), cumulAbsencesParEleve.getOrDefault(abs.getEleve(), 0) + heuresNJ);
            }
        }

        // Filtrer uniquement ceux qui dépassent ou atteignent les 30h réglementaires
        List<Map<String, Object>> resultats = new ArrayList<>();
        cumulAbsencesParEleve.forEach((eleve, totalHeures) -> {
            if (totalHeures >= 30) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", eleve.getId());
                map.put("nom", eleve.getNom());
                map.put("prenom", eleve.getPrenom());
                map.put("email", eleve.getEmail());
                map.put("totalAbsences", totalHeures);
                map.put("dateAlerte", java.time.LocalDate.now().toString());
                resultats.add(map);
            }
        });
        
        return resultats;
    }
}