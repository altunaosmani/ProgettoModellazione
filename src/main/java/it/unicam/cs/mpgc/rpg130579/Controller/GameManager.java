package it.unicam.cs.mpgc.rpg130579.Controller;

import it.unicam.cs.mpgc.rpg130579.Model.Entity.NPC;
import it.unicam.cs.mpgc.rpg130579.Model.Interaction.*;
import it.unicam.cs.mpgc.rpg130579.Model.World.Level;
import it.unicam.cs.mpgc.rpg130579.Model.World.Location;

public class GameManager {
    private final GameState state;

    public GameManager(GameState state) {
        if (state == null) throw new IllegalArgumentException("Lo stato di gioco non può essere nullo");
        this.state = state;
    }

    public GameState getCurrentState() { return state; }

    public Level getCurrentLevel() { return state.getCurrentLevel(); }

    public boolean isGameOver() { return state.isGameEnd(); }

    public boolean hasWon() { return state.isWin(); }

    public Location getLocation(String locationId) {
        if (locationId == null || locationId.isBlank()) throw new IllegalArgumentException("L'ID del luogo non può essere vuoto");
        Location location = getCurrentLevel().getPlaceById(locationId);
        if (location == null) throw new IllegalArgumentException("Luogo non trovato: " + locationId);
        return location;
    }

    public DialogueSession interactWithNPC(String locationId, String npcId) {
        ensureRunning();
        if (npcId == null || npcId.isBlank()) throw new IllegalArgumentException("L'ID del personaggio non può essere vuoto");
        NPC npc = getLocation(locationId).getNPCById(npcId);
        if (npc == null) throw new IllegalArgumentException("Personaggio non trovato: " + npcId);
        return new DialogueSession(npc.interact(state.getPlayer()), state.getProgress());
    }

    public boolean solvePuzzle(String puzzleId, PuzzleAnswer answer) {
        ensureRunning();
        if (answer == null) throw new IllegalArgumentException("La risposta non può essere nulla");
        Puzzle puzzle = getCurrentLevel().getPuzzleById(puzzleId);
        if (puzzle == null) throw new IllegalArgumentException("Enigma non trovato: " + puzzleId);
        boolean solved = puzzle.isSolvedBy(answer, state.getPlayer());
        if (solved) state.getProgress().markPuzzleSolved(puzzleId);
        return solved;
    }

    public boolean completeCurrentQuest() {
        if (state.isGameEnd()) return false;
        Level level = getCurrentLevel();
        if (level.getQuest().isCompleted()) return false;
        for (int i = 0; i < level.getSizePuzzles(); i++) {
            if (!state.getProgress().isPuzzleSolved(level.getPuzzleAt(i).getId())) return false;
        }
        level.getQuest().complete();
        level.complete();
        return true;
    }

    public void advanceToNextLevel() {
        ensureRunning();
        state.nextLevel();
    }

    private void ensureRunning() {
        if (state.isGameEnd()) throw new IllegalStateException("La partita è già terminata");
    }
}