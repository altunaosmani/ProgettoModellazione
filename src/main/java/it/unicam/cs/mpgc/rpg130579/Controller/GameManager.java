package it.unicam.cs.mpgc.rpg130579.Controller;

import it.unicam.cs.mpgc.rpg130579.Model.Interaction.Dialogue;
import it.unicam.cs.mpgc.rpg130579.Model.Interaction.Enigma;
import it.unicam.cs.mpgc.rpg130579.Model.World.Level;
import it.unicam.cs.mpgc.rpg130579.Model.World.Location;
import it.unicam.cs.mpgc.rpg130579.Persistence.GameRepository;
import it.unicam.cs.mpgc.rpg130579.Persistence.SaveData;

public class GameManager {
    private final GameState currentState;
    private final GameRepository repository;

    public GameManager(GameState gameState) {
        if (gameState == null) throw new IllegalArgumentException("Lo stato del gioco non può essere nullo");
        this.currentState = gameState;
        this.repository = new GameRepository();
    }

    public GameManager(GameState gameState, GameRepository repository) {
        if (gameState == null) throw new IllegalArgumentException();
        if (repository == null) throw new IllegalArgumentException();
        this.currentState = gameState;
        this.repository = repository;
    }

    public boolean saveGame(String currentLocationId) {
        String levelIndex = currentState.getCurrentLevel().getId();
        SaveData data = new SaveData(levelIndex, currentLocationId, currentState.isGameEnd());
        return repository.saveGame(data);
    }

    public SaveData load() {
        return repository.loadGame();
    }

    public GameState getCurrentState() {
        return this.currentState;
    }

    public Level getCurrentLevel() {
        return currentState.getCurrentLevel();
    }

    public Location getLocation(String locationId) {
        if (locationId == null || locationId.isBlank()) throw new IllegalArgumentException("L'ID del luogo non può essere vuoto");
        return currentState.getCurrentLevel().getPlaceById(locationId);
    }

    public Dialogue interactWithNPC(String locationId, String npcId) {
        if (currentState.isGameEnd()) throw new IllegalStateException("La partita è già terminata");
        if (locationId == null || locationId.isBlank()) throw new IllegalArgumentException("L'ID del luogo non opuò essere vuoto");
        if (npcId == null || npcId.isBlank()) throw new IllegalArgumentException("L'ID del NPC non può essere vuoto");
        return currentState.getCurrentLevel().getPlaceById(locationId).getNPCById(npcId).getDialogue();
    }

    public boolean solveEnigma(String enigmaId, String answer) {
        if (enigmaId == null || enigmaId.isBlank()) throw new IllegalArgumentException();
        if (answer == null || answer.isBlank()) throw new IllegalArgumentException();
        if (currentState.isGameEnd()) throw new IllegalStateException("La partita è già terminata");
        Enigma enigma = getCurrentLevel().getEnigmaById(enigmaId);
        if (enigma == null) return false;
        return enigma.checkAnswer(answer);
    }

    public boolean completeCurrentQuest() {
        if (currentState.isGameEnd()) return false;
        if (getCurrentLevel().getQuest().isCompleted()) return false;
        for (int i = 0; i < getCurrentLevel().getSizeEnigmas(); i++) {
            Enigma enigma = getCurrentLevel().getEnigmaAt(i);
            if (!enigma.isSolved()) return false;
        }
        getCurrentLevel().getQuest().isCompleted();
        getCurrentLevel().complete();
        return true;
    }

    public void advanceToNextLevel() {
        if (currentState.isGameEnd()) throw new IllegalStateException("Il gioco è già terminato");
        currentState.nextLevel();
    }

    public boolean isGameOver() {
        return currentState.isGameEnd();
    }

    public boolean hasWon() {
        return currentState.isWin();
    }
}
