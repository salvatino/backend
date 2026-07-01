package com.edugest.pro.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal valeur;

    @Column(name = "type_note", nullable = false)
    private String typeNote;

    @Column(nullable = false)
    private Integer trimestre;

    @Column(name = "date_saisie")
    private LocalDateTime dateSaisie = LocalDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "eleve_id")
    private Eleve eleve;

    @Column(nullable = false)
    private String matiere; 
}