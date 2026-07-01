package com.edugest.pro.controllers;

import com.edugest.pro.models.Note;
import com.edugest.pro.services.NoteService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) { this.noteService = noteService; }

    @GetMapping
    public List<Note> getAll() { return noteService.getAllNotes(); }

    @GetMapping("/eleve/{id}")
    public List<Note> getByEleve(@PathVariable Long id) { return noteService.getNotesByEleve(id); }

    @PostMapping
    public Note create(@RequestBody Note note) { return noteService.saveNote(note); }
}