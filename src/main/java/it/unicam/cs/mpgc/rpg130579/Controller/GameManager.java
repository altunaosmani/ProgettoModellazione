package it.unicam.cs.mpgc.rpg130579.Controller;

import it.unicam.cs.mpgc.rpg130579.Model.Interaction.Dialogue;
import it.unicam.cs.mpgc.rpg130579.Model.Interaction.Enigma;
import it.unicam.cs.mpgc.rpg130579.Model.World.Level;
import it.unicam.cs.mpgc.rpg130579.Model.World.Location;

public class GameManager {
    private final GameState gameState;

    public GameManager(GameState gameState) {
        if (gameState == null) throw new IllegalArgumentException("Lo stato del gioco non può essere nullo");
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public Level getCurrentLevel() {
        return gameState.getCurrentLevel();
    }

    public Location getLocation(String locationId) {
        if (locationId == null || locationId.isBlank()) throw new IllegalArgumentException("L'ID del luogo non può essere vuoto");
        return gameState.getCurrentLevel().getPlaceById(locationId);
    }

    public Dialogue interactWithNPC(String locationId, String npcId) {
        if (gameState.isGameEnd()) throw new IllegalStateException("La partita è già terminata");
        if (locationId == null || locationId.isBlank()) throw new IllegalArgumentException("L'ID del luogo non opuò essere vuoto");
        if (npcId == null || npcId.isBlank()) throw new IllegalArgumentException("L'ID del NPC non può essere vuoto");
        return gameState.getCurrentLevel().getPlaceById(locationId).getNPCById(npcId).getDialogue();
    }

    public boolean solveEnigma(String enigmaId, String answer) {
        if (enigmaId == null || enigmaId.isBlank()) throw new IllegalArgumentException();
        if (answer == null || answer.isBlank()) throw new IllegalArgumentException();
        if (gameState.isGameEnd()) throw new IllegalStateException("La partita è già terminata");
        Enigma enigma = getCurrentLevel().getEnigmaById(enigmaId);
        if (enigma == null) return false;
        return enigma.checkAnswer(answer);
    }

    public boolean completeCurrentQuest() {
        if (gameState.isGameEnd()) return false;
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
        if (gameState.isGameEnd()) throw new IllegalStateException("Il gioco è già terminato");
        gameState.nextLevel();
    }

    public boolean isGameOver() {
        return gameState.isGameEnd();
    }

    public boolean hasWon() {
        return gameState.isWin();
    }
}
