package com.edugest.pro.models.enums;

public enum SequenceMinesec {
    SEQ1("1ère Séquence"),
    SEQ2("2ème Séquence"),
    SEQ3("3ème Séquence"),
    SEQ4("4ème Séquence"),
    SEQ5("5ème Séquence"),
    SEQ6("6ème Séquence");

    private final String libelle;

    SequenceMinesec(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}