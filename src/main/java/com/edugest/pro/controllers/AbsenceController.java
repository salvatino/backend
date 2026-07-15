package com.edugest.pro.controllers;

import com.edugest.pro.models.Absence;
import com.edugest.pro.repositories.AbsenceRepository;
import com.edugest.pro.services.AbsenceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/absences")
@CrossOrigin(origins = "http://localhost:3000")
public class AbsenceController {

    @Autowired
    private AbsenceService absenceService;
    private final AbsenceRepository absenceRepository;

    public AbsenceController(AbsenceRepository absenceRepository) {
        this.absenceRepository = absenceRepository;
    }

    @GetMapping("/eleve/{utilisateurId}")
    public ResponseEntity<List<Absence>> getAbsencesByEleve(@PathVariable Long utilisateurId) {
        List<Absence> absences = absenceRepository.findByEleveId(utilisateurId);
        return ResponseEntity.ok(absences);
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<Absence> creerAbsence(@RequestBody Absence absence) {
        Absence resultat = absenceService.enregistrerAbsence(absence);
        return ResponseEntity.ok(resultat);
    }

    @GetMapping("/critiques")
    public ResponseEntity<List<Map<String, Object>>> getElevesSeuilCritique() {
        // Cette méthode va demander au service la liste des élèves ayant >= 30h
        List<Map<String, Object>> elevesCritiques = absenceService.obtenirElevesDepassantSeuil(30);
        return ResponseEntity.ok(elevesCritiques);
    }
}