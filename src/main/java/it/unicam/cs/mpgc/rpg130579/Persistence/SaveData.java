package it.unicam.cs.mpgc.rpg130579.Persistence;

import java.io.Serial;
import java.io.Serializable;

public class SaveData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String playerName;
    private int playerHealth;
    private int currentLevelId;
    private String currentLocationId;

    public SaveData() {

    }

    public SaveData(String playerName, int playerHealth, int currentLevelId, String currentLocationId) {
        if (playerName == null || playerName.isBlank()) throw new IllegalArgumentException("Il nome del giocatore non può essere vuoto");
        if (playerHealth < 0) throw new IllegalArgumentException("Il livello di salute del giocatore non può essere negativo");
        if (currentLevelId < 0) throw new IllegalArgumentException("L'attuale ID del livello non può essere negativo");
        if (currentLocationId == null || currentLocationId.isBlank()) throw new IllegalArgumentException("L'ID del luogo non può essere vuoto");
        this.playerName = playerName;
        this.playerHealth = playerHealth;
        this.currentLevelId = currentLevelId;
        this.currentLocationId = currentLocationId;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerHealth() {
        return this.playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public int getCurrentLevelId() {
        return this.currentLevelId;
    }

    public void setCurrentLevelId(int currentLevelId) {
        this.currentLevelId = currentLevelId;
    }

    public String getCurrentLocationId() {
        return this.currentLocationId;
    }

    public void setCurrentLocationId(String currentLocationId) {
        this.currentLocationId = currentLocationId;
    }
}
