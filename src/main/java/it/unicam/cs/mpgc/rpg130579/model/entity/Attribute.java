package it.unicam.cs.mpgc.rpg130579.model.entity;

/**
 * Enumerazione degli attributi del protagonista che influenzano dialoghi,
 * indizi, dettagli ambientali e finali della storia.
 */
public enum Attribute {
    SAPIENZA("Sapienza"),
    PERSUASIONE("Persuasione"),
    PERCEZIONE("Percezione");

    private final String label;

    Attribute(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}