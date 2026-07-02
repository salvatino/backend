package com.edugest.pro.models;

import com.edugest.pro.models.enums.Jour;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "emplois_temps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmploiTemps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Jour jour; // LUNDI, MARDI, etc.

    @Column(name = "heure_debut", nullable = false)
    private LocalTime heureDebut;

    @Column(name = "heure_fin", nullable = false)
    private LocalTime heureFin;

    @Column(length = 50)
    private String salle;
}