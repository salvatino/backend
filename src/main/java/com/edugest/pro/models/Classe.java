package com.edugest.pro.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "classes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Classe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nom; 

    private String niveau;          // 🎓 Ajouté pour DataInitializer
    private String anneeScolaire;   // 🎓 Ajouté pour DataInitializer
    private int effectifMax;        // 🎓 Ajouté pour DataInitializer

    @OneToMany(mappedBy = "classe")
    private List<Eleve> eleves;
}