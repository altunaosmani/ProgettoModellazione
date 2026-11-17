package it.unicam.cs.mpgc.rpg130579.model.world;

import it.unicam.cs.mpgc.rpg130579.model.interaction.Puzzle;
import it.unicam.cs.mpgc.rpg130579.model.interaction.Quest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Un livello: un insieme di luoghi, enigmi e meccanismi con una missione da completare. */
public class Level {
    private final String id;
    private final String name;
    private final String description;
    private final Quest quest;
    private final List<Location> places = new ArrayList<>();
    private final List<Puzzle> puzzles = new ArrayList<>();
    private final List<Mechanism> mechanisms = new ArrayList<>();
    private String startLocationId;

    public Level(String id, String name, String description, Quest quest) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID del livello non può essere vuoto");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Il nome del livello non può essere vuoto");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("La descrizione del livello non può essere vuota");
        if (quest == null) throw new IllegalArgumentException("Il livello deve avere una missione");
        this.id = id;
        this.name = name;
        this.description = description;
        this.quest = quest;
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

    public boolean isCompleted() {
        return quest.isCompleted();
    }

    // ---------- Luoghi ----------

    public String getStartLocationId() {
        return this.startLocationId;
    }

    public void setStartLocation(String locationId) {
        Location start = findPlaceById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Il luogo iniziale non appartiene al livello: " + locationId));
        this.startLocationId = start.getId();
    }

    public Location getStartLocation() {
        if (startLocationId == null) throw new IllegalStateException("Il luogo iniziale non è stato impostato");
        return getPlaceById(startLocationId);
    }

    public int getSizePlaces() {
        return places.size();
    }

    /** Il luogo con questo id, se esiste (senza distinzione tra maiuscole e minuscole). */
    public Optional<Location> findPlaceById(String placeId) {
        if (placeId == null || placeId.isBlank()) return Optional.empty();
        return places.stream().filter(place -> place.getId().equalsIgnoreCase(placeId)).findFirst();
    }

    /** @throws IllegalArgumentException se l'id è vuoto o il luogo non esiste */
    public Location getPlaceById(String placeId) {
        if (placeId == null || placeId.isBlank()) throw new IllegalArgumentException("L'ID del luogo non può essere vuoto");
        return findPlaceById(placeId).orElseThrow(() -> new IllegalArgumentException("Luogo non trovato: " + placeId));
    }

    public void addPlace(Location place) {
        if (place == null) throw new IllegalArgumentException("Il luogo da aggiungere non può essere nullo");
        if (findPlaceById(place.getId()).isPresent()) throw new IllegalArgumentException("Esiste già un luogo con questo id: " + place.getId());
        places.add(place);
    }

    public Location getPlaceAt(int index) {
        if (index < 0 || index >= places.size()) throw new IllegalArgumentException("Indice del luogo non valido");
        return places.get(index);
    }

    // ---------- Enigmi ----------

    public int getSizePuzzles() {
        return puzzles.size();
    }

    public Optional<Puzzle> findPuzzleById(String puzzleId) {
        if (puzzleId == null || puzzleId.isBlank()) return Optional.empty();
        return puzzles.stream().filter(puzzle -> puzzle.getId().equalsIgnoreCase(puzzleId)).findFirst();
    }

    /** @throws IllegalArgumentException se l'id è vuoto o l'enigma non esiste */
    public Puzzle getPuzzleById(String puzzleId) {
        if (puzzleId == null || puzzleId.isBlank()) throw new IllegalArgumentException("L'ID dell'enigma non può essere vuoto");
        return findPuzzleById(puzzleId).orElseThrow(() -> new IllegalArgumentException("Enigma non trovato: " + puzzleId));
    }

    public void addPuzzle(Puzzle puzzle) {
        if (puzzle == null) throw new IllegalArgumentException("L'enigma non può essere nullo");
        if (findPuzzleById(puzzle.getId()).isPresent()) throw new IllegalArgumentException("Esiste già un enigma con questo id: " + puzzle.getId());
        puzzles.add(puzzle);
    }

    public Puzzle getPuzzleAt(int index) {
        if (index < 0 || index >= puzzles.size()) throw new IllegalArgumentException("Indice dell'enigma non valido");
        return puzzles.get(index);
    }

    // ---------- Meccanismi ----------

    public int getSizeMechanism() {
        return mechanisms.size();
    }

    public Optional<Mechanism> findMechanismById(String mechanismId) {
        if (mechanismId == null || mechanismId.isBlank()) return Optional.empty();
        return mechanisms.stream().filter(mechanism -> mechanism.getId().equalsIgnoreCase(mechanismId)).findFirst();
    }

    /** @throws IllegalArgumentException se l'id è vuoto o il meccanismo non esiste */
    public Mechanism getMechanismById(String mechanismId) {
        if (mechanismId == null || mechanismId.isBlank()) throw new IllegalArgumentException("L'ID del meccanismo non può essere vuoto");
        return findMechanismById(mechanismId).orElseThrow(() -> new IllegalArgumentException("Meccanismo non trovato: " + mechanismId));
    }

    /** Registra il meccanismo e, se richiede un enigma non ancora noto al livello, registra anche quello. */
    public void addMechanism(Mechanism mechanism) {
        if (mechanism == null) throw new IllegalArgumentException("Il meccanismo non può essere nullo");
        if (findMechanismById(mechanism.getId()).isPresent()) throw new IllegalArgumentException("Esiste già un meccanismo con questo id: " + mechanism.getId());
        mechanisms.add(mechanism);
        mechanism.getRequiredPuzzle().filter(p -> findPuzzleById(p.getId()).isEmpty()).ifPresent(puzzles::add);
    }

    public Mechanism getMechanismAt(int index) {
        if (index < 0 || index >= mechanisms.size()) throw new IllegalArgumentException("Indice del meccanismo non valido");
        return mechanisms.get(index);
    }
}