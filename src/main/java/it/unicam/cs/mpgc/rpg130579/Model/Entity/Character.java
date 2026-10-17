package it.unicam.cs.mpgc.rpg130579.Model.Entity;

import it.unicam.cs.mpgc.rpg130579.Model.World.Location;

import java.util.Objects;

public abstract class Character {
    private final String id;
    private String name;
    private String description;
    private Location currentLocation;

    public Character(String id, String name, String description, Location currentLocation) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID del personaggio non può essere nullo");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Il nome del personaggio non può essere nullo");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("La descrizione del personaggio non può essere nulla");
        if (currentLocation == null) throw new IllegalArgumentException("La location non può essere nulla");
        this.id = id;
        this.name = name;
        this.description = description;
        this.currentLocation = currentLocation;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.getDescription();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    public void setCurrentLocation() {
        this.currentLocation = currentLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (this == o) return true;
        Character character = (Character) o;
        return Objects.equals(id, character.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + " (" + getClass().getSimpleName() + ")";
    }
}
