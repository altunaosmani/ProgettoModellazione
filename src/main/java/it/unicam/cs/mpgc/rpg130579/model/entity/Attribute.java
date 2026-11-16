package it.unicam.cs.mpgc.rpg130579.model.entity;

/** Attributi del protagonista. Aggiungere un valore qui basta per introdurre un nuovo attributo. */
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