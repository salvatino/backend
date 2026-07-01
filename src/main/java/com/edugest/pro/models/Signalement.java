package com.edugest.pro.models;

import com.edugest.pro.models.enums.TypeAbus;
import com.edugest.pro.models.enums.StatutSignalement;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "signalements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Signalement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_abus", nullable = false)
    private TypeAbus typeAbus;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "date_signalement")
    private LocalDateTime dateSignalement = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private StatutSignalement statut = StatutSignalement.NON_LU;

    @ManyToOne
    @JoinColumn(name = "eleve_id")
    private Eleve eleve; // Peut rester null si le signalement est fait de façon 100% anonyme
}