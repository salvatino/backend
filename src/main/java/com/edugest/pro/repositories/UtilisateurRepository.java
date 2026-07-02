package com.edugest.pro.repositories;

import com.edugest.pro.models.Utilisateur;
import com.edugest.pro.models.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    
    // Trouver un utilisateur par son email (utilisé pour le login JWT)
    Optional<Utilisateur> findByEmail(String email);
    
    // Vérifier si un email existe déjà (pour l'inscription)
    Boolean existsByEmail(String email);
    
    // Filtrer les utilisateurs par rôle (ex: lister tous les enseignants)
    List<Utilisateur> findByRole(Role role);
}