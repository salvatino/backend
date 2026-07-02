package com.edugest.pro.repositories;

import com.edugest.pro.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    
    // Récupérer toutes les notes d'un élève spécifique
    List<Note> findByEleveId(Long eleveId);
    
    // Récupérer les notes d'un élève pour un trimestre précis (ex: Trimestre 1)
    List<Note> findByEleveIdAndTrimestre(Long eleveId, Integer trimestre);
    
    // Récupérer toutes les notes d'un cours (pour voir les résultats d'un devoir par exemple)
    List<Note> findByCoursId(Long coursId);
}