package it.unicam.cs.mpgc.rpg130579.Persistence;

import java.io.Serial;
import java.io.Serializable;

public class SaveData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int currentLevelIndex;
    private String currentLocationId;
    public boolean isGameEnded;

    public SaveData() {
    }

    public SaveData(int currentLevelInedx, String currentLocationId, boolean isGameEnded) {
        if (currentLevelInedx < 0) throw new IllegalArgumentException("L'attuale ID del livello non può essere negativo");
        if (currentLocationId == null || currentLocationId.isBlank()) throw new IllegalArgumentException("L'ID del luogo non può essere vuoto");
        this.currentLevelIndex = currentLevelInedx;
        this.currentLocationId = currentLocationId;
        this.isGameEnded = isGameEnded;
    }


    public int getCurrentLevelIndex() {
        return this.currentLevelIndex;
    }

    public void setCurrentLevelId(int currentLevelIndex) {
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
