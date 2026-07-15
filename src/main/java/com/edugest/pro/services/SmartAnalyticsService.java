package com.edugest.pro.services;

import com.edugest.pro.models.Note;
import com.edugest.pro.models.enums.SequenceMinesec;
import com.edugest.pro.models.enums.Serie;
import com.edugest.pro.repositories.NoteRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SmartAnalyticsService {

    private final NoteRepository noteRepository;

    public Map<String, Object> analyserPerformancesSequence(Long eleveId, SequenceMinesec sequence) {
        List<Note> notes = noteRepository.findByEleveIdAndSequence(eleveId, sequence);
        Map<String, Object> rapport = new HashMap<>();

        if (notes.isEmpty()) {
            rapport.put("moyenneGenerale", 0.00);
            rapport.put("mentionMinesec", "N/A");
            rapport.put("decisionConseil", "Aucune note saisie pour cette séquence");
            rapport.put("alerteRisqueDecrochage", false);
            rapport.put("messageSmartInsight", "🍃 En attente de l'évaluation des compétences par les enseignants.");
            return rapport;
        }

        BigDecimal totalPoints = BigDecimal.ZERO;
        int totalCoefficients = 0;
        List<String> matieresCritiquesEnEchec = new ArrayList<>();
        
        // On récupère la série de l'élève à partir de la première note
        Serie serieEleve = notes.get(0).getEleve().getSerie();

        for (Note note : notes) {
            BigDecimal coeff = BigDecimal.valueOf(note.getCoefficient());
            // Points cumulés = Note * Coefficient
            totalPoints = totalPoints.add(note.getValeur().multiply(coeff));
            totalCoefficients += note.getCoefficient();

            // 🧠 ALGORITHME SMART : Détection d'une note < 10/20 sur une matière fondamentale
            double valeurDouble = note.getValeur().doubleValue();
            String nomCours = note.getCours().getNom();
            
            if (valeurDouble < 10.0 && isMatiereFondamentale(nomCours, serieEleve)) {
                matieresCritiquesEnEchec.add(nomCours);
            }
        }

        // Calcul de la moyenne générale pondérée (arrondie à 2 décimales)
        BigDecimal moyenneGenerale = BigDecimal.ZERO;
        if (totalCoefficients > 0) {
            moyenneGenerale = totalPoints.divide(BigDecimal.valueOf(totalCoefficients), 2, RoundingMode.HALF_UP);
        }

        double moyDouble = moyenneGenerale.doubleValue();

        // Application des règles et mentions du système éducatif camerounais
        rapport.put("moyenneGenerale", moyDouble);
        rapport.put("mentionMinesec", obtenirMentionCameroun(moyDouble));
        rapport.put("decisionConseil", obtenirDecisionConseil(moyDouble));
        
        // Si l'élève a une moyenne générale sous 10 OU s'il échoue dans un pilier de sa série
        boolean enRisque = moyDouble < 10.0 || !matieresCritiquesEnEchec.isEmpty();
        rapport.put("alerteRisqueDecrochage", enRisque);
        
        // Génération du texte prédictif intelligent
        rapport.put("messageSmartInsight", genererSmartInsight(moyDouble, matieresCritiquesEnEchec, serieEleve));

        return rapport;
    }

    private boolean isMatiereFondamentale(String nomCours, Serie serie) {
        String coursLower = nomCours.toLowerCase();
        if (serie == Serie.SERIE_D) {
            return coursLower.contains("svt") || coursLower.contains("biologie") || coursLower.contains("math");
        }
        if (serie == Serie.SERIE_C) {
            return coursLower.contains("math") || coursLower.contains("physique") || coursLower.contains("chimie");
        }
        if (serie == Serie.SERIE_A) {
            return coursLower.contains("littérature") || coursLower.contains("philo") || coursLower.contains("langue");
        }
        return false;
    }

    private String obtenirMentionCameroun(double moy) {
        if (moy >= 16) return "Très Bien";
        if (moy >= 14) return "Bien";
        if (moy >= 12) return "Assez Bien";
        if (moy >= 10) return "Passable";
        if (moy >= 8) return "Médiocre";
        return "Mauvais";
    }

    private String obtenirDecisionConseil(double moy) {
        if (moy >= 12) return "Tableau d'Honneur (TH) avec Félicitations";
        if (moy >= 10) return "Admis - Encouragements du Conseil";
        if (moy >= 8) return "Blâme de travail / Risque de redoublement";
        return "Avertissement de travail / Risque d'exclusion de l'établissement";
    }

    private String genererSmartInsight(double moyenne, List<String> enEchec, Serie serie) {
        if (moyenne < 10.0 && !enEchec.isEmpty()) {
            return "🚨 Alerte Rouge EduGest : Performance globale critique pour la " + serie.name() + 
                   ". Échec direct sur vos piliers de spécialité : " + enEchec + ". Un encadrement d'urgence est impératif.";
        } else if (!enEchec.isEmpty()) {
            return "⚠️ Vigilance : Votre moyenne est sauvée par les matières secondaires, mais le système a détecté un niveau fragile dans vos matières majeures " + enEchec + ". Attention au goulot d'étranglement aux examens officiels.";
        } else if (moyenne >= 14.0) {
            return "🎯 Algorithme EduGest : Excellent profil. Aptitudes confirmées pour le second cycle " + serie.name() + ". Potentiel fort pour les bourses universitaires.";
        }
        return "✅ Profil académique équilibré et stable. Poursuivez l'effort de régularité.";
    }
}