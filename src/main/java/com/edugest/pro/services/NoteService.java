package com.edugest.pro.services;

import com.edugest.pro.models.Note;
import com.edugest.pro.repositories.NoteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getAllNotes() { return noteRepository.findAll(); }
    public List<Note> getNotesByEleve(Long eleveId) { return noteRepository.findByEleveId(eleveId); }
    public Note saveNote(Note note) { return noteRepository.save(note); }
}