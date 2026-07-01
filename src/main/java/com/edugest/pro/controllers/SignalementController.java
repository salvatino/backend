package com.edugest.pro.controllers;

import com.edugest.pro.models.Signalement;
import com.edugest.pro.services.SignalementService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/signalements")
public class SignalementController {
    private final SignalementService signalementService;

    public SignalementController(SignalementService signalementService) {
        this.signalementService = signalementService;
    }

    @PostMapping
    public Signalement create(@RequestBody Signalement s) {
        return signalementService.creerSignalement(s);
    }

    @GetMapping
    public List<Signalement> getAll() {
        return signalementService.recupererTousLesSignalements();
    }
}