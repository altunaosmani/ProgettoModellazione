package it.unicam.cs.mpgc.rpg130579;

import java.util.ArrayList;
import java.util.List;

public class GestoreLivelli {
    private final List<Level> level;
    private int indexCurrentLevel;

    public GestoreLivelli() {
        this.level = new ArrayList<>();
        this.indexCurrentLevel = 0;
    }

    public int getLevelsSize() {
        return level.size();
    }

    public Level getCurrentLevel(){
        if (level.isEmpty()) return null;
        return level.get(indexCurrentLevel);
    }

    public Level getLevelById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException();
        for (Level livello : level) {
            if (livello.getId().equalsIgnoreCase(id)) return livello;
        }
        return null;
    }

    public void addLevel(Level l) {
        if (l == null) throw new IllegalArgumentException();
        if (getLevelById(l.getId()) != null) throw new IllegalArgumentException();
        level.add(l);
    }

    public Level getLevelAt(int index) {
        if (index < 0 || index >= level.size()) throw new IllegalArgumentException();
        return level.get(index);
    }

    public boolean nextLevel() {
        Level current = getCurrentLevel();
        if (current == null || current.isCompleted()) return false;
        if (indexCurrentLevel >= level.size() - 1) return false;
        indexCurrentLevel++;
        return true;
    }

    public boolean lastLevel() {
        if (!level.isEmpty() && indexCurrentLevel == level.size() - 1) return true;
        return false;
    }


}
