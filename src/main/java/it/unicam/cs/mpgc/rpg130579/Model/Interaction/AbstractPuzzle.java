package it.unicam.cs.mpgc.rpg130579.Model.Interaction;

import java.util.Objects;

public abstract class AbstractPuzzle implements Puzzle {
    private final String id;
    private final String description;

    protected AbstractPuzzle(String id, String description) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID dell'enigma non può essere vuoto");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("La descrizione dell'enigma non può essere vuota");
        this.id = id;
        this.description = description;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id.equals(((AbstractPuzzle) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + id + ")";
    }
}
