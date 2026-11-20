package it.unicam.cs.mpgc.rpg130579.model.world;

import it.unicam.cs.mpgc.rpg130579.model.interaction.Puzzle;
import it.unicam.cs.mpgc.rpg130579.model.interaction.Quest;
import java.util.*;

/**
 * Rappresenta un singolo capitolo/livello di gioco, contenente i luoghi ({@link Location}),
 * gli enigmi ({@link Puzzle}), i meccanismi ({@link Mechanism}), il punto di partenza
 * e la {@link Quest} da completare.
 */
public class Level {
    private final String id;
    private final String name;
    private final String description;
    private final Quest quest;
    private final Map<String, Location> places = new LinkedHashMap<>();
    private final Map<String, Puzzle> puzzles = new LinkedHashMap<>();
    private final Map<String, Mechanism> mechanisms = new LinkedHashMap<>();
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

    private static String key(String id) {
        return id.trim().toLowerCase(Locale.ROOT);
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

    public List<Location> getPlaces() {
        return List.copyOf(places.values());
    }

    public List<Mechanism> getMechanisms() {
        return List.copyOf(mechanisms.values());
    }

    public String getStartLocationId() {
        return this.startLocationId;
    }

    public Location getStartLocation() {
        if (startLocationId == null) throw new IllegalStateException("Il luogo iniziale non è stato impostato");
        return getPlaceById(startLocationId);
    }

    public void setStartLocation(String locationId) {
        Location start = findPlaceById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Il luogo iniziale non appartiene al livello: " + locationId));
        this.startLocationId = start.getId();
    }

    public Optional<Location> findPlaceById(String placeId) {
        if (placeId == null || placeId.isBlank()) return Optional.empty();
        return Optional.ofNullable(places.get(key(placeId)));
    }

    public Location getPlaceById(String placeId) {
        if (placeId == null || placeId.isBlank()) throw new IllegalArgumentException("L'ID del luogo non può essere vuoto");
        return findPlaceById(placeId).orElseThrow(() -> new IllegalArgumentException("Luogo non trovato: " + placeId));
    }

    public void addPlace(Location place) {
        if (place == null) throw new IllegalArgumentException("Il luogo da aggiungere non può essere nullo");
        if (places.putIfAbsent(key(place.getId()), place) != null) {
            throw new IllegalArgumentException("Esiste già un luogo con questo id: " + place.getId());
        }
    }

    public Optional<Puzzle> findPuzzleById(String puzzleId) {
        if (puzzleId == null || puzzleId.isBlank()) return Optional.empty();
        return Optional.ofNullable(puzzles.get(key(puzzleId)));
    }

    public Puzzle getPuzzleById(String puzzleId) {
        if (puzzleId == null || puzzleId.isBlank()) throw new IllegalArgumentException("L'ID dell'enigma non può essere vuoto");
        return findPuzzleById(puzzleId).orElseThrow(() -> new IllegalArgumentException("Enigma non trovato: " + puzzleId));
    }

    public void addPuzzle(Puzzle puzzle) {
        if (puzzle == null) throw new IllegalArgumentException("L'enigma non può essere nullo");
        if (puzzles.putIfAbsent(key(puzzle.getId()), puzzle) != null) {
            throw new IllegalArgumentException("Esiste già un enigma con questo id: " + puzzle.getId());
        }
    }

    public Optional<Mechanism> findMechanismById(String mechanismId) {
        if (mechanismId == null || mechanismId.isBlank()) return Optional.empty();
        return Optional.ofNullable(mechanisms.get(key(mechanismId)));
    }

    public Mechanism getMechanismById(String mechanismId) {
        if (mechanismId == null || mechanismId.isBlank()) throw new IllegalArgumentException("L'ID del meccanismo non può essere vuoto");
        return findMechanismById(mechanismId).orElseThrow(() -> new IllegalArgumentException("Meccanismo non trovato: " + mechanismId));
    }

    public void addMechanism(String locationId, Mechanism mechanism) {
        if (mechanism == null) throw new IllegalArgumentException("Il meccanismo non può essere nullo");
        Location place = getPlaceById(locationId);
        if (mechanisms.putIfAbsent(key(mechanism.getId()), mechanism) != null) {
            throw new IllegalArgumentException("Esiste già un meccanismo con questo id: " + mechanism.getId());
        }
        mechanism.getRequiredPuzzle().ifPresent(p -> puzzles.putIfAbsent(key(p.getId()), p));
        place.addMechanism(mechanism);
    }
}