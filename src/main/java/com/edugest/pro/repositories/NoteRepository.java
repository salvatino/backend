package com.edugest.pro.repositories;

import com.edugest.pro.models.Note;
import com.edugest.pro.models.enums.SequenceMinesec; 
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    
    // Récupérer toutes les notes d'un élève spécifique
    List<Note> findByEleveId(Long eleveId);
    
    // Récupérer toutes les notes d'un cours (pour voir les résultats d'un devoir par exemple)
    List<Note> findByCoursId(Long coursId);

    // 🧠 AUTOMATISME SMART ANCHOR : Récupère toutes les notes d'un élève pour une séquence précise MINESEC (1 à 6)
    List<Note> findByEleveIdAndSequence(Long eleveId, SequenceMinesec sequence);
}