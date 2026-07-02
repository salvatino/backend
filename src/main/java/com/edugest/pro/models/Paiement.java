package com.edugest.pro.models;

import com.edugest.pro.models.enums.StatutPaiement;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "eleve_id", nullable = false)
    private Eleve eleve;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPaiement statut; // COMPLET, PARTIEL, IMPAYE

    @Column(name = "date_paiement", updatable = false)
    private LocalDateTime datePaiement;

    @PrePersist
    protected void onCreate() {
        this.datePaiement = LocalDateTime.now();
    }
}