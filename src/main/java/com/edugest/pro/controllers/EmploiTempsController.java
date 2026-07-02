package com.edugest.pro.controllers;

import com.edugest.pro.models.Cours;
import com.edugest.pro.repositories.CoursRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/emploi") // On garde votre URL d'origine
@CrossOrigin(origins = "http://localhost:3000") // Indispensable pour votre frontend React
public class EmploiTempsController {

    private final CoursRepository coursRepository;

    // Injection directe du Repository (Pas besoin de Service intermédiaire !)
    public EmploiTempsController(CoursRepository coursRepository) {
        this.coursRepository = coursRepository;
    }

    // 1. Récupérer l'emploi du temps pour une classe (ex: Terminale C)
    @GetMapping("/classe/{classeId}")
    public ResponseEntity<List<Cours>> getEmploiParClasse(@PathVariable Long classeId) {
        List<Cours> planning = coursRepository.findByClasseId(classeId);
        if (planning.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 si aucun cours n'est trouvé
        }
        return ResponseEntity.ok(planning); // 200 OK avec les vrais cours de la BD
    }

    // 2. Récupérer le planning d'un enseignant spécifique
    @GetMapping("/prof/{profId}")
    public ResponseEntity<List<Cours>> getEmploiParEnseignant(@PathVariable Long profId) {
        List<Cours> planning = coursRepository.findByEnseignantId(profId);
        if (planning.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(planning);
    }
}