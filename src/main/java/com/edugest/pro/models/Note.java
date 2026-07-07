package com.edugest.pro.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.edugest.pro.models.enums.TypeNote; 
import com.edugest.pro.models.enums.SequenceMinesec; // Nouvelle énumération
import com.edugest.pro.models.enums.Serie;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "eleve_id", nullable = false)
    @JsonIgnoreProperties({"notes", "enseignant", "classe"})
    private Eleve eleve;

    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    @JsonIgnoreProperties({"notes", "enseignant", "classe"})
    private Cours cours;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal valeur; // Note sur 20 (ex: 15.50)

    @Enumerated(EnumType.STRING)
    @Column(name = "type_note", nullable = false)
    private TypeNote typeNote; // DEVOIR, EXAMEN, TP, INTERROGATION

    // 🇨🇲 REFONTÉ MINESEC : Remplacement du trimestre par la Séquence (1 à 6)
    @Enumerated(EnumType.STRING)
    @Column(name = "sequence", nullable = false)
    private SequenceMinesec sequence;

    // 🇨🇲 AJOUTS SMART BULLETIN
    @Column(nullable = false)
    private Integer coefficient; // Calculé dynamiquement par le système

    private String appreciation; // Pour les remarques du conseil de classe

    @Column(name = "date_saisie", updatable = false)
    private LocalDateTime dateSaisie;

    @PrePersist
    protected void onCreate() {
        this.dateSaisie = LocalDateTime.now();
        this.calculerEtInjecterCoefficient();
    }

    /**
     * 🧠 LOGIQUE D'AUTOMATISATION DES COEFFICIENTS (Système Camerounais)
     * Détermine le coefficient officiel basé sur la série de l'élève et l'intitulé du cours.
     */
    private void calculerEtInjecterCoefficient() {
        if (this.eleve == null || this.eleve.getSerie() == null || this.cours == null) {
            this.coefficient = 2; // Coefficient par défaut par sécurité
            return;
        }

        Serie serieEleve = this.eleve.getSerie();
        String nomCours = this.cours.getNom().toLowerCase(); // En supposant que l'entité Cours a une méthode getNom()

        switch (serieEleve) {
            case SERIE_D:
                if (nomCours.contains("svt") || nomCours.contains("biologie") || nomCours.contains("nature")) this.coefficient = 5;
                else if (nomCours.contains("math")) this.coefficient = 4;
                else if (nomCours.contains("physique") || nomCours.contains("chimie")) this.coefficient = 4;
                else if (nomCours.contains("français") || nomCours.contains("anglais") || nomCours.contains("langue")) this.coefficient = 3;
                else this.coefficient = 2; // Histoire-Géo, ECM, Philo...
                break;

            case SERIE_C:
                if (nomCours.contains("math")) this.coefficient = 6;
                else if (nomCours.contains("physique") || nomCours.contains("chimie")) this.coefficient = 5;
                else if (nomCours.contains("français") || nomCours.contains("anglais")) this.coefficient = 3;
                else this.coefficient = 2; // SVT, Histoire...
                break;

            case SERIE_A:
                if (nomCours.contains("littérature") || nomCours.contains("philo")) this.coefficient = 4;
                else if (nomCours.contains("histoire") || nomCours.contains("géo") || nomCours.contains("ecm")) this.coefficient = 3;
                else if (nomCours.contains("allemand") || nomCours.contains("espagnol")) this.coefficient = 3;
                else this.coefficient = 2; // Mathématiques réduites en A
                break;
                
            default:
                this.coefficient = 2;
        }
    }
}