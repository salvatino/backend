package com.edugest.pro.controllers;

import com.edugest.pro.models.enums.SequenceMinesec;
import com.edugest.pro.services.SmartAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permet de répondre aux requêtes de ton app React sans blocage CORS
public class SmartAnalyticsController {

    private final SmartAnalyticsService smartAnalyticsService;

    @GetMapping("/eleve/{eleveId}/sequence/{sequence}")
    public ResponseEntity<Map<String, Object>> obtenirRapportPedagogique(
            @PathVariable Long eleveId,
            @PathVariable SequenceMinesec sequence) {
        
        Map<String, Object> diagnostic = smartAnalyticsService.analyserPerformancesSequence(eleveId, sequence);
        return ResponseEntity.ok(diagnostic);
    }
}