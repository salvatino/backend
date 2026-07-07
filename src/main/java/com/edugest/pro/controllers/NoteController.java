package com.edugest.pro.controllers;

import com.edugest.pro.models.Eleve;
import com.edugest.pro.models.Note;
import com.edugest.pro.repositories.EleveRepository;
import com.edugest.pro.repositories.NoteRepository; // 💡 AJOUT DE L'IMPORT
import com.edugest.pro.services.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "http://localhost:3000")
public class NoteController {

    private final NoteService noteService; 
    private final EleveRepository eleveRepository;
    private final NoteRepository noteRepository; // 💡 INJECTION DIRECTE DU REPOSITORY

    // Constructeur mis à jour avec les 3 dépendances
    public NoteController(NoteService noteService, EleveRepository eleveRepository, NoteRepository noteRepository) {
        this.noteService = noteService;
        this.eleveRepository = eleveRepository;
        this.noteRepository = noteRepository;
    }

    @GetMapping("/eleve/{utilisateurId}")
public ResponseEntity<List<Note>> getNotesByEleveUtilisateurId(@PathVariable Long utilisateurId) {
    
    // 1. On récupère toutes les notes pour la démo
    List<Note> toutesLesNotes = noteRepository.findAll();
    
    // 2. IMPORTANT : Il faut renvoyer la liste à l'intérieur du corps de la ResponseEntity !
    return ResponseEntity.ok(toutesLesNotes); 
}

    // Tes autres méthodes existantes qui utilisent noteService restent ici et ne bougent pas !
}