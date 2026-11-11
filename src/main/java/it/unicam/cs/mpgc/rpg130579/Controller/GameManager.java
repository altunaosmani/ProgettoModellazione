package it.unicam.cs.mpgc.rpg130579.Controller;

import it.unicam.cs.mpgc.rpg130579.Model.Entity.Item;
import it.unicam.cs.mpgc.rpg130579.Model.Entity.NPC;
import it.unicam.cs.mpgc.rpg130579.Model.Entity.Player;
import it.unicam.cs.mpgc.rpg130579.Model.Interaction.*;
import it.unicam.cs.mpgc.rpg130579.Model.World.Exit;
import it.unicam.cs.mpgc.rpg130579.Model.World.Level;
import it.unicam.cs.mpgc.rpg130579.Model.World.Location;
import it.unicam.cs.mpgc.rpg130579.Model.World.Mechanism;

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

    public void advanceToNextLevel() {
        ensureRunning();
        state.nextLevel();
    }

    private void ensureRunning() {
        if (state.isGameEnd()) throw new IllegalStateException("La partita è già terminata");
    }

    public boolean move(String exitId) {
        ensureRunning();
        Player player = state.getPlayer();
        Exit exit = player.getCurrentLocation().getExitById(exitId);
        if (exit == null) throw new IllegalArgumentException("Uscita non trovata: " + exitId);
        if (!exit.isPassable()) return false;
        player.setCurrentLocation(getLocation(exit.getDestinationLocationId()));
        return true;
    }

    public boolean takeItem(String itemId) {
        ensureRunning();
        Player player = state.getPlayer();
        Location location = player.getCurrentLocation();
        Item item = location.getItemsById(itemId);
        if (item == null) throw new IllegalArgumentException("Oggetto non trovato: " + itemId);
        if (!player.addItem(item)) return false;
        location.removeItem(itemId);
        return true;
    }

    public boolean unlockMechanism(String mechanismId, PuzzleAnswer answer) {
        ensureRunning();
        Player player = state.getPlayer();
        Mechanism mechanism = player.getCurrentLocation().getMechanismById(mechanismId);
        if (mechanism == null) throw new IllegalArgumentException("Meccanismo non trovato: " + mechanismId);
        return mechanism.tryUnlock(player, answer);
    }

    public boolean completeCurrentQuest() {
        if (state.isGameEnd()) return false;
        Level level = getCurrentLevel();
        Quest quest = level.getQuest();
        if (!quest.isCompletable(state.getPlayer(), state.getProgress())) return false;
        quest.complete();
        level.complete();
        return true;
    }
}