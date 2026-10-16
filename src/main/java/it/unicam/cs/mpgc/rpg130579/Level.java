package it.unicam.cs.mpgc.rpg130579;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private final String id;
    private final String name;
    private final String description;
    private final List<Location> places;
    private final List<Enigma> enigmas;
    private final Quest quest;
    private boolean completed;

    public Level(String id, String name, String description, Quest quest) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID del livello non può essere vuoto");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Il nome del livello non può essere vuoto");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("La descrizione del livello non deve essere vuoto");
        if (quest == null) throw new IllegalArgumentException("Il livello deve avere almeno una quest");
        this.id = id;
        this.name = name;
        this.description = description;
        this.places = new ArrayList<>();
        this.enigmas = new ArrayList<>();
        this.quest = quest;
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

    public int getSizePlaces() {
        return this.places.size();
    }

    public int getSizeEnigmas() {
        return this.enigmas.size();
    }

    public Quest getQuest() {
        return this.quest;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public Location getPlaceById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException();
        for (Location place : places) {
            if (getId().equalsIgnoreCase(id)) return place;
        }
        return null;
    }

    public void addPlace(Location l) {
        if (l == null) throw new IllegalArgumentException();
        if (getPlaceById(l.getId()) != null) throw new IllegalArgumentException("Esiste già un luogo on questo id");
        places.add(l);
    }

    public Location getPlaceAt(int index) {
        if (index < 0 || index >= places.size()) throw new IllegalArgumentException("Indice del luogo non valido");
        return places.get(index);
    }

    public Enigma getEnigmaById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException();
        for (Enigma enigma : enigmas) {
            if (enigma.getId().equalsIgnoreCase(id)) return enigma;
        }
        return null;
    }

    public void addEnigma(Enigma e) {
        if (e == null) throw new IllegalArgumentException();
        if (getEnigmaById(e.getId()) != null) throw new IllegalArgumentException("Esiste già un enigma con questo id");
        enigmas.add(e);
    }

    public Enigma getEnigmaAt(int index) {
       if (index < 0 || index >= enigmas.size()) throw new IllegalArgumentException("L'indice non è adatto");
       return enigmas.get(index);
    }

    public void complete() {
        this.completed = true;
    }
}
