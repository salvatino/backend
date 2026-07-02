package com.edugest.pro.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "matieres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Matiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom; // Ex: Mathématiques, Informatique
}
