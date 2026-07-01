package com.edugest.pro.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/emploi")
public class EmploiTempsController {
    @GetMapping
    public List<?> getEmploi() {
        return List.of(
            Map.of("jour", "Lundi", "matiere", "Mathématiques", "heure", "08h-10h"),
            Map.of("jour", "Mardi", "matiere", "Histoire-Géo", "heure", "10h-12h")
        );
    }
}