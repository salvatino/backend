package com.edugest.pro.controllers;

import com.edugest.pro.models.Signalement;
import com.edugest.pro.services.SignalementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/signalements")
@CrossOrigin(origins = "http://localhost:3000") // Connexion avec React
public class SignalementController {

    private final SignalementService signalementService;

    public SignalementController(SignalementService signalementService) {
        this.signalementService = signalementService;
    }

    // 1. Les élèves ou utilisateurs déposent un signalement
    @PostMapping
    public ResponseEntity<Signalement> creer(@RequestBody Signalement signalement) {
        Signalement nouveau = signalementService.sauvegarder(signalement);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveau);
    }

    // 2. L'administration consulte la liste des signalements
    @GetMapping
    public ResponseEntity<List<Signalement>> getAll() {
        List<Signalement> signalements = signalementService.obtenirTousLesSignalements();
        return ResponseEntity.ok(signalements);
    }
}