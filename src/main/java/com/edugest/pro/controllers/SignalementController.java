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
    // 2. L'administration consulte la liste des signalements
    @GetMapping
    public ResponseEntity<List<Signalement>> getAll() {
        List<Signalement> signalements = signalementService.obtenirTousLesSignalements();
        
        // Sécurité supplémentaire : s'assurer que les signalements anonymes n'embarquent aucune trace
        signalements.forEach(s -> {
            if (s.isAnonyme()) {
                s.setEleve(null);
            }
        });
        
        return ResponseEntity.ok(signalements);
    }

    @GetMapping("/mon-historique/{eleveId}")
    public ResponseEntity<List<Signalement>> getMonHistorique(@PathVariable Long eleveId) {
        // Une méthode dans ton service qui fait un: return repository.findByEleveId(eleveId);
        List<Signalement> mesSignalements = signalementService.obtenirParEleve(eleveId); 
        return ResponseEntity.ok(mesSignalements);
    }

    // 4. Modifier le statut d'un signalement (ex: /api/signalements/5/statut?statut=EN_COURS)
    @PatchMapping("/{id}/statut")
    public ResponseEntity<Signalement> majStatut(
            @PathVariable Long id, 
            @RequestParam String statut) {
        
        Signalement misAJour = signalementService.changerStatut(id, statut);
        return ResponseEntity.ok(misAJour);
    }
}