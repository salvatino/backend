package com.edugest.pro.config;

import com.edugest.pro.models.*;
import com.edugest.pro.models.enums.Role;
import com.edugest.pro.models.enums.TypeNote;
import com.edugest.pro.models.enums.TypeAbus;
import com.edugest.pro.models.enums.StatutSignalement;
import com.edugest.pro.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            UtilisateurRepository utilisateurRepository,
            ClasseRepository classeRepository,
            MatiereRepository matiereRepository,
            CoursRepository coursRepository,
            EleveRepository eleveRepository,
            NoteRepository noteRepository,
            SignalementRepository signalementRepository) {
        
        return args -> {
            // 1. On vérifie si la base contient déjà des données
            if (utilisateurRepository.count() > 0) {
                System.out.println("👉 Base de données déjà alimentée, saut de l'initialisation.");
                return;
            }

            System.out.println("🚀 Initialisation des données de test Métier - EduGest Pro...");

            // ========================================================
            // COMPTE ADMINISTRATEUR
            // ========================================================
            Utilisateur admin = new Utilisateur();
            admin.setNom("TCHAMENI");
            admin.setPrenom("Samuel");
            admin.setEmail("admin@edugest.pro");
            admin.setMotDePasse("admin123");
            admin.setRole(Role.ADMIN);
            utilisateurRepository.save(admin);


            // 2. Création d'une classe d'exemple (Contexte Cameroun)
            Classe tleC = new Classe();
            tleC.setNom("Terminale C");
            tleC.setNiveau("Second Cycle");
            tleC.setAnneeScolaire("2025-2026");
            tleC.setEffectifMax(60);
            tleC = classeRepository.save(tleC);


            // 3. Création des matières
            Matiere maths = new Matiere();
            maths.setNom("Mathématiques");
            maths = matiereRepository.save(maths);

            Matiere info = new Matiere();
            info.setNom("Informatique / Java");
            info = matiereRepository.save(info);


            // 4. Création d'un Enseignant
            Utilisateur prof = new Utilisateur();
            prof.setNom("Foko");
            prof.setPrenom("Jean-Pierre");
            prof.setEmail("prof.foko@edugest.cm");
            prof.setMotDePasse("password123"); 
            prof.setRole(Role.ENSEIGNANT);
            prof = utilisateurRepository.save(prof);


            // 5. Création des Cours
            Cours coursMathsTleC = new Cours();
            coursMathsTleC.setEnseignant(prof);
            coursMathsTleC.setClasse(tleC);
            coursMathsTleC.setMatiere(maths);
            coursMathsTleC.setCoefficient(5); 
            coursMathsTleC.setVolumeHoraire(120);
            coursMathsTleC = coursRepository.save(coursMathsTleC); // Réassigné pour l'utiliser pour les notes

            Cours coursInfoTleC = new Cours();
            coursInfoTleC.setEnseignant(prof);
            coursInfoTleC.setClasse(tleC);
            coursInfoTleC.setMatiere(info);
            coursInfoTleC.setCoefficient(3); 
            coursInfoTleC.setVolumeHoraire(60);
            coursInfoTleC = coursRepository.save(coursInfoTleC); // Réassigné pour l'utiliser pour les notes


            // 6. Création de l'Élève : Marc Ebolo
            Utilisateur compteEleve = new Utilisateur();
            compteEleve.setNom("Ebolo");
            compteEleve.setPrenom("Marc");
            compteEleve.setEmail("marc.ebolo@eleve.cm");
            compteEleve.setMotDePasse("password123");
            compteEleve.setRole(Role.ELEVE);
            compteEleve = utilisateurRepository.save(compteEleve); 

            Eleve eleveMarc = new Eleve();
            eleveMarc.setUtilisateur(compteEleve); 
            eleveMarc.setClasse(tleC);
            eleveMarc.setMatricule("26EG001");
            eleveMarc.setDateNaissance(LocalDate.of(2009, 5, 14));
            eleveMarc.setTelephone("+237677000000");
            eleveMarc = eleveRepository.save(eleveMarc);


            // ========================================================
            // SCÉNARIO DE DÉMO 1 : LES NOTES DE MARC EBOLO (Alignées sur ton entité)
            // ========================================================
            Note noteMaths = new Note();
            noteMaths.setEleve(eleveMarc);
            noteMaths.setCours(coursMathsTleC); // Correction : Lié au cours
            noteMaths.setValeur(BigDecimal.valueOf(14.50)); // Correction : Passage en BigDecimal
            noteMaths.setTypeNote(TypeNote.DEVOIR); // Alignement Enum
            noteMaths.setTrimestre(1);
            noteRepository.save(noteMaths);

            Note noteInfo = new Note();
            noteInfo.setEleve(eleveMarc);
            noteInfo.setCours(coursInfoTleC); // Correction : Lié au cours
            noteInfo.setValeur(BigDecimal.valueOf(18.00)); // Correction : Passage en BigDecimal
            noteInfo.setTypeNote(TypeNote.EXAMEN); // Alignement Enum
            noteInfo.setTrimestre(1);
            noteRepository.save(noteInfo);


            // ========================================================
            // SCÉNARIO DE DÉMO 2 : INCIDENTS / SIGNALEMENTS (Alignés sur ton entité)
            // ========================================================
            Signalement sig1 = new Signalement();
            sig1.setEleve(eleveMarc);
            
            // Remplace TypeAbus.HARCELEMENT par une autre valeur de ton Enum TypeAbus si nécessaire
            sig1.setTypeAbus(TypeAbus.HARCELEMENT); 
            
            sig1.setDescription("Signalement d'un incident de comportement récurrent dans la cour de récréation.");
            sig1.setStatut(StatutSignalement.RESOLU); // Alignement Enum (NON_LU, EN_COURS ou RESOLU)
            
            // Remarque : Pas besoin de setDateSignalement(), ton @PrePersist s'en charge tout seul !
            signalementRepository.save(sig1);

            System.out.println("✅ Données de test métier injectées sans aucune erreur !");
        };
    }
}