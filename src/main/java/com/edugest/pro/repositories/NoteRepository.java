package com.edugest.pro.repositories;

import com.edugest.pro.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByEleveId(Long eleveId);
}