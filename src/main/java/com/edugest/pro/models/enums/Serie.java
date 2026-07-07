package com.edugest.pro.models.enums;

public enum Serie {
    SERIE_A("Littéraire"),
    SERIE_C("Mathématiques & Sciences Physiques"),
    SERIE_D("Sciences de la Vie et de la Terre");

    private final String description;

    Serie(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}