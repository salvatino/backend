package com.edugest.pro.models;

import com.edugest.pro.models.enums.TypeAbus;
import com.edugest.pro.models.enums.StatutSignalement;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "signalements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Signalement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "eleve_id") // Sans "nullable = false" pour permettre l'anonymat !
    private Eleve eleve;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_abus", nullable = false)
    private TypeAbus typeAbus;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_signalement", updatable = false)
    private LocalDateTime dateSignalement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutSignalement statut = StatutSignalement.NON_LU;

    @PrePersist
    protected void onCreate() {
        this.dateSignalement = LocalDateTime.now();
    }

    public boolean isAnonyme() {
        return this.eleve == null;
    }

    public String getTitre() {
        
        throw new UnsupportedOperationException("Unimplemented method 'getTitre'");
    }
}