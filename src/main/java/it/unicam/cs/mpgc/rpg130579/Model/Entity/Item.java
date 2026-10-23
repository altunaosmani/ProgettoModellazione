package it.unicam.cs.mpgc.rpg130579.Model.Entity;

import java.util.Objects;

public abstract class Item {
    private final String id;
    private final String name;
    private final String description;

    public Item(String id, String name, String description) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID dell'oggetto non può essere vuoto");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Il nome dell'oggetto non può essere vuoto");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("La descrizione dell'oggetto non può essere vuota");
        this.id = id;
        this.name = name;
        this.description = description;
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

    public abstract boolean use(Object target);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name;
    }
}
