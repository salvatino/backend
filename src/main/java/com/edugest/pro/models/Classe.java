package com.edugest.pro.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nom; // Ex: 6eme M1, Tle C

    @Column(nullable = false, length = 50)
    private String niveau; // Ex: Premier Cycle, Second Cycle

    @Column(name = "annee_scolaire", nullable = false, length = 10)
    private String anneeScolaire; // Ex: 2025-2026

    @Column(name = "effectif_max")
    private Integer effectifMax = 60;

    @OneToMany(mappedBy = "classe")
    private List<Eleve> eleves;

}
