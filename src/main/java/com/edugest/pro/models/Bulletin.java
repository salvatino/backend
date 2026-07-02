package com.edugest.pro.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "bulletins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bulletin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "eleve_id", nullable = false)
    private Eleve eleve;

    @Column(nullable = false)
    private Integer trimestre; // 1, 2 ou 3

    @Column(name = "moyenne_generale", nullable = false, precision = 5, scale = 2)
    private BigDecimal moyenneGenerale;

    private Integer rang;

    @Column(length = 255)
    private String appreciation;

    private Boolean valide = false;
}