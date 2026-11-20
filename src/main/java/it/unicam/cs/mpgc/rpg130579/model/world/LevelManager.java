package it.unicam.cs.mpgc.rpg130579.model.world;

import java.util.*;

/**
 * Gestisce la sequenza ordinata dei livelli di una partita e tiene traccia del livello attivo.
 */
public class LevelManager {
    private final Map<String, Level> levelsById = new LinkedHashMap<>();
    private final List<Level> orderedLevels = new ArrayList<>();
    private int indexCurrentLevel = 0;

    private static String key(String id) {
        return id.trim().toLowerCase(Locale.ROOT);
    }

    public int getLevelsSize() {
        return orderedLevels.size();
    }

    public Level getCurrentLevel() {
        if (orderedLevels.isEmpty()) throw new IllegalStateException("Non ci sono livelli");
        return orderedLevels.get(indexCurrentLevel);
    }

    public int getIndexCurrentLevel() {
        return this.indexCurrentLevel;
    }

    public Optional<Level> findLevelById(String id) {
        if (id == null || id.isBlank()) return Optional.empty();
        return Optional.ofNullable(levelsById.get(key(id)));
    }

    public Level getLevelById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID del livello non può essere vuoto");
        return findLevelById(id).orElseThrow(() -> new IllegalArgumentException("Livello non trovato: " + id));
    }

    public void addLevel(Level level) {
        if (level == null) throw new IllegalArgumentException("Il livello non può essere nullo");
        if (levelsById.putIfAbsent(key(level.getId()), level) != null) {
            throw new IllegalArgumentException("Esiste già un livello con questo id: " + level.getId());
        }
        orderedLevels.add(level);
    }

    public Level getLevelAt(int index) {
        if (index < 0 || index >= orderedLevels.size()) {
            throw new IllegalArgumentException("Indice del livello non valido");
        }
        return orderedLevels.get(index);
    }

    public boolean nextLevel() {
        if (orderedLevels.isEmpty() || !getCurrentLevel().isCompleted()) return false;
        if (isLastLevel()) return false;
        indexCurrentLevel++;
        return true;
    }

    public boolean isLastLevel() {
        return !orderedLevels.isEmpty() && indexCurrentLevel == orderedLevels.size() - 1;
    }

    public boolean setCurrentLevelById(String id) {
        if (id == null || id.isBlank()) return false;
        Level target = levelsById.get(key(id));
        if (target == null) return false;
        indexCurrentLevel = orderedLevels.indexOf(target);
        return true;
    }

    public List<Level> getLevels() {
        return Collections.unmodifiableList(orderedLevels);
    }
}