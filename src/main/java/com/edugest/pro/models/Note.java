package com.edugest.pro.models;

import com.edugest.pro.models.enums.TypeNote; 
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
    private Eleve eleve;

    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal valeur; // Note sur 20 (ex: 15.50)

    @Enumerated(EnumType.STRING)
    @Column(name = "type_note", nullable = false)
    private TypeNote typeNote; // DEVOIR, EXAMEN, TP, INTERROGATION

    @Column(nullable = false)
    private Integer trimestre; // 1, 2 ou 3

    @Column(name = "date_saisie", updatable = false)
    private LocalDateTime dateSaisie;

    @PrePersist
    protected void onCreate() {
        this.dateSaisie = LocalDateTime.now();
    }
}