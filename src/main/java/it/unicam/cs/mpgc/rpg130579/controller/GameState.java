package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;
import it.unicam.cs.mpgc.rpg130579.model.world.LevelManager;
import it.unicam.cs.mpgc.rpg130579.model.world.Level;

import java.util.Locale;

public class GameState {
    private final Player player;
    private final LevelManager gameLevel;
    private final StoryProgress progress;
    private boolean gameEnd;
    private boolean win;

    public GameState(Player player, LevelManager gameLevel) {
        if (player == null) throw new IllegalArgumentException("Il giocatore non può essere nullo");
        if (gameLevel == null) throw new IllegalArgumentException("La partita deve avere almeno un livello");
        this.player = player;
        this.gameLevel = gameLevel;
        this.progress = new StoryProgress();
        this.gameEnd = false;
        this.win = false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public LevelManager getGameLevel() {
        return this.gameLevel;
    }

    public int getCurrentLevelIndex() {
        return this.gameLevel.getIndexCurrentLevel();
    }

    public Level getCurrentLevel() {
        return gameLevel.getCurrentLevel();
    }

    public StoryProgress getProgress() {
        return progress;
    }

    public boolean isGameEnd() {
        return this.gameEnd;
    }

    public boolean isWin() {
        return this.win;
    }

    public void endGame() {
        this.gameEnd = true;
    }

    public void endGameVictory() {
        if (gameEnd) throw new IllegalStateException("La partita è già terminata");
        if (!gameLevel.lastLevel() || !getCurrentLevel().isCompleted()) throw new IllegalStateException("L'ultimo livello deve essere completato per vincere ");
        this.win = true;
        this.gameEnd = true;
    }

    public void restoreEnded(boolean won) {
        this.gameEnd = true;
        this.win = won;
    }

    public void nextLevel() {
        if (gameEnd) throw new IllegalStateException("La partita è già terminata");
        if (gameLevel.nextLevel()) {
            player.setCurrentLocation(getCurrentLevel().getStartLocation());
            return;
        }        if (gameLevel.lastLevel() && getCurrentLevel().isCompleted()) {
            endGameVictory();
        }
        else throw new IllegalStateException("Non è possibile avanzare al prossimo livello");
    }

    public String normalize(String puzzleId) {
        if (puzzleId == null || puzzleId.isBlank())
            throw new IllegalStateException("L'ID dell'enigma non può essere vuoto");
        return puzzleId.trim().toLowerCase(Locale.ROOT);
    }
}
