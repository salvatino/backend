package com.edugest.pro.config;

import com.edugest.pro.models.*;
import com.edugest.pro.models.enums.Role;
import com.edugest.pro.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            UtilisateurRepository utilisateurRepository,
            ClasseRepository classeRepository,
            MatiereRepository matiereRepository,
            CoursRepository coursRepository,
            EleveRepository eleveRepository) {
        
        return args -> {
            // 1. On vérifie si la base contient déjà des utilisateurs pour ne pas dupliquer au second démarrage
            if (utilisateurRepository.count() > 0) {
                System.out.println("👉 Base de données déjà alimentée, saut de l'initialisation.");
                return;
            }

            System.out.println("🚀 Initialisation des données de test EduGest Pro...");

            // 2. Création d'une classe d'exemple (Contexte Cameroun)
            Classe tleC = new Classe();
            tleC.setNom("Terminale C");
            tleC.setNiveau("Second Cycle");
            tleC.setAnneeScolaire("2025-2026");
            tleC.setEffectifMax(60);
            tleC = classeRepository.save(tleC);

            // 3. Création d'une matière
            Matiere maths = new Matiere();
            maths.setNom("Mathématiques");
            maths.setId(null); // Laisser l'auto-increment agir
            maths = matiereRepository.save(maths);

            // 4. Création d'un Enseignant (Compte Utilisateur)
            Utilisateur prof = new Utilisateur();
            prof.setNom("Foko");
            prof.setPrenom("Jean-Pierre");
            prof.setEmail("prof.foko@edugest.cm");
            // Note: En production, ce mot de passe devra être encodé avec BCryptPasswordEncoder
            prof.setMotDePasse("password123"); 
            prof.setRole(Role.ENSEIGNANT);
            prof = utilisateurRepository.save(prof);

            // 5. Création du Cours (Lien entre Prof, Matière et Classe)
            // Note : Assure-toi que ton entité Cours possède un constructeur ou des setters fonctionnels
            Cours coursMathsTleC = new Cours();
            coursMathsTleC.setEnseignant(prof);
            coursMathsTleC.setClasse(tleC);
            coursMathsTleC.setMatiere(maths);
            coursMathsTleC.setCoefficient(5); // Fort coef en Terminale C !
            coursMathsTleC.setVolumeHoraire(120);
            coursRepository.save(coursMathsTleC);

            // 6. Création d'un Élève
            // Étape A : Son compte utilisateur global
            Utilisateur compteEleve = new Utilisateur();
            compteEleve.setNom("Ebolo");
            compteEleve.setPrenom("Marc");
            compteEleve.setEmail("marc.ebolo@eleve.cm");
            compteEleve.setMotDePasse("password123");
            compteEleve.setRole(Role.ELEVE);

            // /!\ TRÈS IMPORTANT : On réassigne avec le résultat du .save()
            compteEleve = utilisateurRepository.save(compteEleve); 

            // Étape B : Son profil Élève lié à sa classe
            Eleve eleve = new Eleve();
            eleve.setUtilisateur(compteEleve); // Ici on passe l'entité managée
            eleve.setClasse(tleC);
            eleve.setMatricule("26EG001");
            eleve.setDateNaissance(LocalDate.of(2009, 5, 14));
            eleve.setTelephone("+237677000000");

            eleveRepository.save(eleve);

            System.out.println("✅ Données de test injectées avec succès !");
        };
    }
}