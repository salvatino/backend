package com.edugest.pro.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.edugest.pro.models.enums.Serie; // On va créer cette énumération juste en dessous

@Entity
@Table(name = "eleves")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Eleve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String matricule;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    private String telephone;

    // 🇨🇲 AJOUT MINESEC : La série de l'élève pour le calcul intelligent des coefficients
    @Enumerated(EnumType.STRING)
    @Column(name = "serie", nullable = false)
    private Serie serie; 

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "utilisateur_id", referencedColumnName = "id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe; // Lien vers la table classes

    @OneToMany(mappedBy = "eleve")
    @JsonIgnore
    private List<Signalement> signalements;

    // 🇨🇲 Passerelles vers Utilisateur pour simplifier l'accès au nom et prénom
    public String getNom() {
        return (this.utilisateur != null) ? this.utilisateur.getNom() : "Nom inconnu";
    }

    public String getPrenom() {
        return (this.utilisateur != null) ? this.utilisateur.getPrenom() : "Prénom inconnu";
    }
}