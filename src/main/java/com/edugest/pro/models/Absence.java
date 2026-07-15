package com.edugest.pro.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "absences")
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int heuresJustifiees;
    private int heuresNonJustifiees;
    private String sequence; // Ex: "SEQ1", "SEQ2"
    private LocalDate dateEnregistrement;

    @ManyToOne
    @JoinColumn(name = "eleve_id")
    private Utilisateur eleve; // Ou ton entité "Eleve" selon ton architecture

    // Constructeurs, Getters et Setters
    public Absence() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getHeuresJustifiees() { return heuresJustifiees; }
    public void setHeuresJustifiees(int heuresJustifiees) { this.heuresJustifiees = heuresJustifiees; }
    public int getHeuresNonJustifiees() { return heuresNonJustifiees; }
    public void setHeuresNonJustifiees(int heuresNonJustifiees) { this.heuresNonJustifiees = heuresNonJustifiees; }
    public String getSequence() { return sequence; }
    public void setSequence(String sequence) { this.sequence = sequence; }
    public LocalDate getDateEnregistrement() { return dateEnregistrement; }
    public void setDateEnregistrement(LocalDate dateEnregistrement) { this.dateEnregistrement = dateEnregistrement; }
    public Utilisateur getEleve() { return eleve; }
    public void setEleve(Utilisateur eleve) { this.eleve = eleve; }
}