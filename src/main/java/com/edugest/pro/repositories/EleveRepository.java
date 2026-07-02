package com.edugest.pro.repositories;

import com.edugest.pro.models.Eleve;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EleveRepository extends JpaRepository<Eleve, Long> {
    
    // Trouver un élève par son matricule unique
    Optional<Eleve> findByMatricule(String matricule);
    
    // Lister tous les élèves inscrits dans une classe spécifique
    List<Eleve> findByClasseId(Long classeId);
    
    // Trouver l'élève lié à un compte utilisateur global
    Optional<Eleve> findByUtilisateurId(Long utilisateurId);
}