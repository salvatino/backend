package com.edugest.pro.controllers;

import com.edugest.pro.models.Note;
import com.edugest.pro.repositories.EleveRepository;
import com.edugest.pro.repositories.NoteRepository;
import com.edugest.pro.services.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "http://localhost:3000")
public class NoteController {

    private final NoteService noteService; 
    private final EleveRepository eleveRepository;
    private final NoteRepository noteRepository;

    public NoteController(NoteService noteService, EleveRepository eleveRepository, NoteRepository noteRepository) {
        this.noteService = noteService;
        this.eleveRepository = eleveRepository;
        this.noteRepository = noteRepository;
    }

    // 1. Permettre à l'élève connecté de voir la liste complète
    @GetMapping("/eleve/{utilisateurId}")
    public ResponseEntity<List<Note>> getNotesByEleveUtilisateurId(@PathVariable Long utilisateurId) {
        List<Note> toutesLesNotes = noteRepository.findAll();
        return ResponseEntity.ok(toutesLesNotes); 
    }

    // 🎯 AJOUT ENSEIGNANT : Saisie collective d'un devoir ou examen pour toute la classe
    @PostMapping("/saisie-collective")
    public ResponseEntity<String> sauvegarderNotes(@RequestBody List<Note> notes) {
        if (notes == null || notes.isEmpty()) {
            return ResponseEntity.badRequest().body("La liste des notes transmise est vide.");
        }
        noteRepository.saveAll(notes);
        return ResponseEntity.ok("Toutes les notes de la séquence ont été enregistrées avec succès !");
    }
}