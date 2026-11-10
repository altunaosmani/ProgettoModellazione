package it.unicam.cs.mpgc.rpg130579.Model.World;

import it.unicam.cs.mpgc.rpg130579.Model.Interaction.Puzzle;
import it.unicam.cs.mpgc.rpg130579.Model.Interaction.Quest;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private final String id;
    private final String name;
    private final String description;
    private String startLocationId;
    private final List<Location> places;
    private final List<Puzzle> puzzles;
    private final List<Mechanism> mechanisms;
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
        this.startLocationId = startLocationId;
        this.places = new ArrayList<>();
        this.puzzles = new ArrayList<>();
        this.mechanisms = new ArrayList<>();
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

    public Quest getQuest() {
        return this.quest;
    }

    public String getStartLocationId() {
        return this.startLocationId;
    }

    public void setStartLocation(String locationId) {
        if (getPlaceById(locationId) == null) throw new IllegalArgumentException("Il luogo iniziale non appartiene al livello: " + locationId);
        this.startLocationId = locationId;
    }

    public Location getStartLocation() {
        if (startLocationId == null) throw new IllegalArgumentException("Il luogo iniziale non è stato impostato");
        return getPlaceById(startLocationId);
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void complete() {
        this.completed = true;
    }

    public int getSizePlaces() {
        return this.places.size();
    }

    public Location getPlaceById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID del luogo non può essere vuoto");
        for (Location place : places) {
            if (place.getId().equalsIgnoreCase(id)) return place;
        }
        return null;
    }

    public void addPlace(Location l) {
        if (l == null) throw new IllegalArgumentException("Il luogo da aggiungere non può essere nullo");
        if (getPlaceById(l.getId()) != null) throw new IllegalArgumentException("Esiste già un luogo on questo id");
        places.add(l);
    }

    public Location getPlaceAt(int index) {
        if (index < 0 || index >= places.size()) throw new IllegalArgumentException("Indice del luogo non valido");
        return places.get(index);
    }

    public int getSizePuzzles() {
        return this.puzzles.size();
    }

    public Puzzle getPuzzleById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException();
        for (Puzzle puzzle : puzzles) {
            if (puzzle.getId().equalsIgnoreCase(id)) return puzzle;
        }
        return null;
    }

    public void addPuzzle(Puzzle p) {
        if (p == null) throw new IllegalArgumentException();
        if (getPuzzleById(p.getId()) != null) throw new IllegalArgumentException("Esiste già un enigma con questo id");
        puzzles.add(p);
    }

    public Puzzle getPuzzleAt(int index) {
       if (index < 0 || index >= puzzles.size()) throw new IllegalArgumentException("L'indice non è adatto");
       return puzzles.get(index);
    }

    public int getSizeMechanism() {
        return this.mechanisms.size();
    }

    public Mechanism getMechanismById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID del meccanismo non può essere vuoto");
        for (Mechanism mechanism : mechanisms) {
            if (mechanism.getId().equalsIgnoreCase(id)) return mechanism;
        }
        return null;
    }

    public void addMechanism(Mechanism m) {
        if (m == null) throw new IllegalArgumentException("Il meccanismo non può essere nullo");
        if (getMechanismById(m.getId()) != null) throw new IllegalArgumentException("Esiste già un meccanismo con questo ID");
        mechanisms.add(m);
    }

    public Mechanism getMechanismAt(int index) {
        if (index < 0 || index >= mechanisms.size()) throw new IllegalArgumentException("Indice del meccanismo non valido");
        return mechanisms.get(index);
    }
}
