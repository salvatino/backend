package com.edugest.pro.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enseignant_id", nullable = false)
    private Utilisateur enseignant; // Doit avoir le rôle ENSEIGNANT

    @ManyToOne
    @JoinColumn(name = "classe_id", nullable = false)
    private Classe classe;

    @ManyToOne
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;

    @Column(nullable = false)
    private Integer coefficient = 2;

    @Column(name = "volume_horaire", nullable = false)
    private Integer volumeHoraire;
}
