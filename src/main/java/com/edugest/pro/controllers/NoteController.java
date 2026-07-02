package com.edugest.pro.controllers;

import com.edugest.pro.models.Note;
import com.edugest.pro.services.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "http://localhost:3000") // Permet à React (port 3000) d'appeler ce contrôleur sans blocage CORS
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) { 
        this.noteService = noteService; 
    }

    // 1. Récupérer toutes les notes
    @GetMapping
    public ResponseEntity<List<Note>> getAll() { 
        List<Note> notes = noteService.getAllNotes();
        return ResponseEntity.ok(notes); // Renvoie un statut 200 OK avec la liste
    }

    // 2. Récupérer les notes d'un élève spécifique
    @GetMapping("/eleve/{id}")
    public ResponseEntity<List<Note>> getByEleve(@PathVariable Long id) { 
        List<Note> notes = noteService.getNotesByEleve(id);
        if (notes.isEmpty()) {
            return ResponseEntity.noContent().build(); // Renvoie 204 si l'élève n'a pas encore de notes
        }
        return ResponseEntity.ok(notes);
    }

    // 3. Saisir une nouvelle note (par un enseignant)
    @PostMapping
    public ResponseEntity<Note> create(@RequestBody Note note) { 
        try {
            Note nouvelleNote = noteService.saveNote(note);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleNote); // Renvoie 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Renvoie 400 si les données sont invalides
        }
    }
}