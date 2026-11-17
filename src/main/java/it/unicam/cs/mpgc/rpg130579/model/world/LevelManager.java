package it.unicam.cs.mpgc.rpg130579.model.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Gestisce la sequenza dei livelli e quale di essi è quello corrente. */
public class LevelManager {
    private final List<Level> levels = new ArrayList<>();
    private int indexCurrentLevel = 0;

    public int getLevelsSize() {
        return levels.size();
    }

    public Level getCurrentLevel() {
        if (levels.isEmpty()) throw new IllegalStateException("Non ci sono livelli");
        return levels.get(indexCurrentLevel);
    }

    public int getIndexCurrentLevel() {
        return this.indexCurrentLevel;
    }

    /** Il livello con questo id, se esiste (senza distinzione tra maiuscole e minuscole). */
    public Optional<Level> findLevelById(String id) {
        if (id == null || id.isBlank()) return Optional.empty();
        return levels.stream().filter(level -> level.getId().equalsIgnoreCase(id)).findFirst();
    }

    /** @throws IllegalArgumentException se l'id è vuoto o il livello non esiste */
    public Level getLevelById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID del livello non può essere vuoto");
        return findLevelById(id).orElseThrow(() -> new IllegalArgumentException("Livello non trovato: " + id));
    }

    public void addLevel(Level level) {
        if (level == null) throw new IllegalArgumentException("Il livello non può essere nullo");
        if (findLevelById(level.getId()).isPresent()) throw new IllegalArgumentException("Esiste già un livello con questo id: " + level.getId());
        levels.add(level);
    }

    public Level getLevelAt(int index) {
        if (index < 0 || index >= levels.size()) throw new IllegalArgumentException("Indice del livello non valido");
        return levels.get(index);
    }

    /** Passa al livello successivo solo se quello corrente è completato e ne esiste uno dopo. */
    public boolean nextLevel() {
        if (levels.isEmpty() || !getCurrentLevel().isCompleted()) return false;
        if (isLastLevel()) return false;
        indexCurrentLevel++;
        return true;
    }

    public boolean isLastLevel() {
        return !levels.isEmpty() && indexCurrentLevel == levels.size() - 1;
    }

    public boolean setCurrentLevelById(String id) {
        for (int i = 0; i < levels.size(); i++) {
            if (levels.get(i).getId().equalsIgnoreCase(id)) {
                indexCurrentLevel = i;
                return true;
            }
        }
        return false;
    }
}