package it.unicam.cs.mpgc.rpg130579.model.world;

import java.util.ArrayList;
import java.util.List;

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

    public Level getLevelById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID del livello non può essere vuoto");
        for (Level level : levels) {
            if (level.getId().equalsIgnoreCase(id)) return level;
        }
        return null;
    }

    public void addLevel(Level level) {
        if (level == null) throw new IllegalArgumentException("Il livello non può essere nullo");
        if (getLevelById(level.getId()) != null) throw new IllegalArgumentException("Esiste già un livello con questo id: " + level.getId());
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
