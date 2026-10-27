package it.unicam.cs.mpgc.rpg130579.Persistence;

import java.io.Serial;
import java.io.Serializable;

public class SaveData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String currentLevelIndex;
    private String currentLocationId;
    public boolean isGameEnded;

    public SaveData() {

    }

    public SaveData(String currentLevelIndex, String currentLocationId, boolean isGameEnded) {
        if (currentLevelIndex == null|| currentLevelIndex.isBlank()) throw new IllegalArgumentException("L'attuale ID del livello non può essere vuoto");
        if (currentLocationId == null || currentLocationId.isBlank()) throw new IllegalArgumentException("L'ID del luogo non può essere vuoto");
        this.currentLevelIndex = currentLevelIndex;
        this.currentLocationId = currentLocationId;
        this.isGameEnded = isGameEnded;
    }

    public String getCurrentLevelIndex() {
        return this.currentLevelIndex;
    }

    public void setCurrentLevelIndex(String currentLevelIndex) {
        this.currentLevelIndex = currentLevelIndex;
    }

    public String getCurrentLocationId() {
        return this.currentLocationId;
    }

    public void setCurrentLocationId(String currentLocationId) {
        this.currentLocationId = currentLocationId;
    }

    public boolean isGameEnded() {
        return isGameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        isGameEnded = gameEnded;
    }
}
