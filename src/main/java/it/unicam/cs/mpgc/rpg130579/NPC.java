package it.unicam.cs.mpgc.rpg130579;

public class NPC {
    private final String name;
    private final String id;
    private final String description;
    private final Dialogue dialogue;

    public NPC(String name, String id, String description, Dialogue dialogue) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Il nome non può essere buoto");
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID non può essere vuoto");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("La descrizione non può essere vuota");
        if (dialogue == null) throw new IllegalArgumentException("Ogni personaggio deve avere un dialogo");
        this.name = name;
        this.id = id;
        this.description = description;
        this.dialogue = dialogue;
    }

    public String getName() {
        return this.name;
    }

    public String getID() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public Dialogue getDialogo() {
        return this.dialogue;
    }
}
