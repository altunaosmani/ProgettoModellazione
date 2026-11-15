package it.unicam.cs.mpgc.rpg130579.model.entity;

import it.unicam.cs.mpgc.rpg130579.model.world.Location;

import java.util.Objects;

public abstract class GameCharacter {
    private final String id;
    private String name;
    private String description;
    private Location currentLocation;

    public GameCharacter(String id, String name, String description, Location currentLocation) {
        this.id = requireText(id, "L'ID del personaggio");
        this.name = requireText(name, "Il nome del personaggio");
        this.description = requireText(description, "La descrizione del personaggio");
        if (currentLocation == null) throw new IllegalArgumentException("Il luogo non può essere nullo");
        this.currentLocation = currentLocation;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = requireText(name, "Il nome del personaggio");
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = requireText(description, "La descrizione del personaggio");
    }

    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        if (currentLocation == null) throw new IllegalArgumentException("Il luogo non può essere nullo");
        this.currentLocation = currentLocation;
    }

    private static String requireText(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " non può essere vuoto");
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((GameCharacter) o).id);
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
