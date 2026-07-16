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
import com.edugest.pro.models.enums.SequenceMinesec;
import com.edugest.pro.models.enums.Serie;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
            if (utilisateurRepository.count() > 0) {
                System.out.println("👉 Base de données déjà alimentée, saut de l'initialisation.");
                return;
            }

            System.out.println("🚀 Initialisation des données de test Métier - EduGest Pro (10 Matières & IA)...");

            // ========================================================
            // 1. COMPTE ADMINISTRATEUR
            // ========================================================
            Utilisateur admin = new Utilisateur();
            admin.setNom("METINO");
            admin.setPrenom("Salvatoré");
            admin.setEmail("admin@edugest.pro");
            admin.setMotDePasse("admin123");
            admin.setRole(Role.ADMIN);
            utilisateurRepository.save(admin);

            // ========================================================
            // 2. CLASSE D'EXEMPLE
            // ========================================================
            Classe tleD = new Classe();
            tleD.setNom("Terminale D1");
            tleD.setNiveau("Second Cycle");
            tleD.setAnneeScolaire("2025-2026");
            tleD.setEffectifMax(60);
            tleD = classeRepository.save(tleD);

            // ========================================================
            // 3. CRÉATION DES 10 MATIÈRES DU MINESEC
            // ========================================================
            String[] nomsMatieres = {
                "Mathématiques", "Physique-Chimie-Technologie", "Informatique & TI", 
                "Langue Française", "English Language", "Histoire-Géographie", 
                "Philosophie", "Sciences de la Vie et de la Terre", 
                "Éducation Civique et Morale", "Littérature Africaine"
            };

            Map<String, Matiere> matieresMap = new HashMap<>();
            for (String nom : nomsMatieres) {
                Matiere mat = new Matiere();
                mat.setNom(nom);
                mat = matiereRepository.save(mat);
                matieresMap.put(nom, mat);
            }

            // ========================================================
            // 4. CRÉATION DES ENSEIGNANTS
            // ========================================================
            Utilisateur prof1 = new Utilisateur();
            prof1.setNom("Foko");
            prof1.setPrenom("Jean-Pierre");
            prof1.setEmail("prof.foko@edugest.cm");
            prof1.setMotDePasse("password123"); 
            prof1.setRole(Role.ENSEIGNANT);
            prof1 = utilisateurRepository.save(prof1);

            Utilisateur prof2 = new Utilisateur();
            prof2.setNom("Ngo");
            prof2.setPrenom("Marie-Thérèse");
            prof2.setEmail("prof.ngo@edugest.cm");
            prof2.setMotDePasse("password123"); 
            prof2.setRole(Role.ENSEIGNANT);
            prof2 = utilisateurRepository.save(prof2);

            // ========================================================
            // 5. CRÉATION DES COURS ASSOCIES AVEC LEURS COEFFICIENTS
            // ========================================================
            Map<String, Integer> coefficients = Map.of(
                "Mathématiques", 4,
                "Physique-Chimie-Technologie", 3,
                "Informatique & TI", 3,
                "Langue Française", 3,
                "English Language", 3,
                "Histoire-Géographie", 2,
                "Philosophie", 2,
                "Sciences de la Vie et de la Terre", 2,
                "Éducation Civique et Morale", 1,
                "Littérature Africaine", 2
            );

            Map<String, Cours> coursMap = new HashMap<>();
            for (String nomMat : nomsMatieres) {
                Cours cours = new Cours();
                // Assigner prof1 aux matières scientifiques, prof2 aux littéraires/sciences humaines
                boolean estScientifique = nomMat.equals("Mathématiques") || nomMat.equals("Physique-Chimie-Technologie") || nomMat.equals("Informatique & TI") || nomMat.equals("Sciences de la Vie et de la Terre");
                cours.setEnseignant(estScientifique ? prof1 : prof2);
                cours.setClasse(tleD);
                cours.setMatiere(matieresMap.get(nomMat));
                cours.setCoefficient(coefficients.get(nomMat));
                cours.setVolumeHoraire(estScientifique ? 120 : 60);
                cours = coursRepository.save(cours);
                coursMap.put(nomMat, cours);
            }

            // ========================================================
            // 6. CRÉATION DE L'ÉLÈVE : MARC EBOLO
            // ========================================================
            Utilisateur compteEleve = new Utilisateur();
            compteEleve.setNom("Ebolo");
            compteEleve.setPrenom("Marc");
            compteEleve.setEmail("marc.ebolo@eleve.cm");
            compteEleve.setMotDePasse("password123");
            compteEleve.setRole(Role.ELEVE);
            compteEleve = utilisateurRepository.save(compteEleve); 

            Eleve eleveMarc = new Eleve();
            eleveMarc.setUtilisateur(compteEleve); 
            eleveMarc.setClasse(tleD);
            eleveMarc.setMatricule("26EG001");
            eleveMarc.setDateNaissance(LocalDate.of(2009, 5, 14));
            eleveMarc.setSerie(Serie.SERIE_D);
            eleveMarc.setTelephone("+237677000000");
            eleveMarc = eleveRepository.save(eleveMarc);

            // ========================================================
            // 7. INJECTION DES NOTES SÉQUENTIELLES POUR LES 10 MATIÈRES
            // ========================================================
            Map<String, Double> notesInitiales = Map.of(
                "Mathématiques", 14.5,
                "Physique-Chimie-Technologie", 12.0,
                "Informatique & TI", 16.5,
                "Langue Française", 11.0,
                "English Language", 13.5,
                "Histoire-Géographie", 10.0,
                "Philosophie", 8.5, // Note faible pour simuler l'IA
                "Sciences de la Vie et de la Terre", 12.5,
                "Éducation Civique et Morale", 15.0,
                "Littérature Africaine", 9.0  // Note faible pour simuler l'IA
            );

            for (String nomMat : nomsMatieres) {
                Note note = new Note();
                note.setEleve(eleveMarc);
                note.setCours(coursMap.get(nomMat));
                note.setValeur(BigDecimal.valueOf(notesInitiales.get(nomMat)));
                note.setTypeNote(TypeNote.DEVOIR);
                note.setSequence(SequenceMinesec.SEQ1);
                noteRepository.save(note);
            }

            // ========================================================
            // 8. CRÉATION D'UN SIGNALEMENT INITIAL
            // ========================================================
            Signalement sig1 = new Signalement();
            sig1.setEleve(eleveMarc);
            sig1.setTypeAbus(TypeAbus.HARCELEMENT); 
            sig1.setDescription("Signalement préventif enregistré pour suivi d'accompagnement.");
            sig1.setStatut(StatutSignalement.RESOLU);
            signalementRepository.save(sig1);

            System.out.println("✅ Données de test enrichies (10 matières distinctes) injectées avec succès !");
        };
    }
}