package it.unicam.cs.mpgc.rpg130579;

public class Quest {
    private final String id;
    private final String name;
    private final String description;
    private boolean completed;

    public Quest(String id, String name, String description) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID della missione non può essere vuoto");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Il nome della missione non può essere vuoto");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("La descrizione della missione non può essere vuota");
        this.id = id;
        this.name = name;
        this.description = description;
        this.completed = false;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void completato() {
        this.completed = true;
    }
}
