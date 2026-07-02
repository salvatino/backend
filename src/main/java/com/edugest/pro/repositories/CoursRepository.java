package com.edugest.pro.repositories;

import com.edugest.pro.models.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CoursRepository extends JpaRepository<Cours, Long> {
    // Permet de récupérer l'emploi du temps (les cours) d'une classe
    List<Cours> findByClasseId(Long classeId);

    // Permet de récupérer le planning d'un enseignant
    List<Cours> findByEnseignantId(Long enseignantId);
}