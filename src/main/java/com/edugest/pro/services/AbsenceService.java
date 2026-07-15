package com.edugest.pro.services;

import com.edugest.pro.models.Absence;
import com.edugest.pro.models.Utilisateur;
import com.edugest.pro.repositories.AbsenceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@Service
public class AbsenceService {

    private final AbsenceRepository absenceRepository;
    // Injecte ici ton service ou repository de notification si tu en as un

    public AbsenceService(AbsenceRepository absenceRepository) {
        this.absenceRepository = absenceRepository;
    }

    public Absence enregistrerAbsence(Absence nouvelleAbsence) {
        // 1. Sauvegarde de l'absence ajoutée par l'enseignant
        Absence absenceSauvegardee = absenceRepository.save(nouvelleAbsence);

        // 2. Calcul du cumul des heures non justifiées de l'élève pour l'année/séquence
        Long eleveId = nouvelleAbsence.getEleve().getId();
        List<Absence> toutesLesAbsences = absenceRepository.findByEleveId(eleveId);

        int cumulNonJustifiees = toutesLesAbsences.stream()
                .mapToInt(Absence::getHeuresNonJustifiees)
                .sum();

        // 3. Logique d'alerte MINESEC (Seuil des 30h)
        if (cumulNonJustifiees >= 30) {
            declencherAlerteProviseur(nouvelleAbsence.getEleve(), cumulNonJustifiees);
        }

        return absenceSauvegardee;
    }

    private void declencherAlerteProviseur(Utilisateur eleve, int heures) {
        // Ici, tu peux insérer une ligne dans une table "notifications" ou changer un statut
        System.out.println("🚨 ALERTE PROVISEUR : L'élève " + eleve.getPrenom() + " " + eleve.getNom() 
            + " a atteint " + heures + "h d'absences non justifiées. Procédure d'exclusion requise.");
        
        // Exemple : eleve.setStatutDisclinaire("CONSEIL_DISCIPLINE_REQUIS");
    }

    public List<Map<String, Object>> obtenirElevesDepassantSeuil(int seuilHeures) {
        List<Map<String, Object>> resultat = new ArrayList<>();
        
        // 1. Récupérer toutes les absences de la BDD
        List<Absence> toutesLesAbsences = absenceRepository.findAll();
        
        // 2. Grouper par élève et calculer le cumul (Logique Stream Java 8+)
        Map<Utilisateur, Integer> cumulParEleve = toutesLesAbsences.stream()
            .collect(Collectors.groupingBy(
                Absence::getEleve,
                Collectors.summingInt(Absence::getHeuresNonJustifiees)
            ));
        
        // 3. Filtrer ceux qui dépassent le seuil (ex: 30h)
        cumulParEleve.forEach((eleve, totalHeures) -> {
            if (totalHeures >= seuilHeures) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", eleve.getId());
                map.put("nom", eleve.getNom());
                map.put("prenom", eleve.getPrenom());
                map.put("classe", "Terminale D1"); // À dynamiser selon ton modèle Utilisateur/Classe
                map.put("totalAbsences", totalHeures);
                map.put("dateAlerte", java.time.LocalDate.now().toString());
                resultat.add(map);
            }
        });
        
        return resultat;
    }
}