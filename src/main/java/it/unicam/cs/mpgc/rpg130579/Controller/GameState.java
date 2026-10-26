package it.unicam.cs.mpgc.rpg130579.Controller;

import it.unicam.cs.mpgc.rpg130579.Model.Entity.Player;
import it.unicam.cs.mpgc.rpg130579.Model.World.LevelManager;
import it.unicam.cs.mpgc.rpg130579.Model.World.Level;

public class GameState {
    private final Player player;
    private final LevelManager gameLevel;
    private boolean gameEnd;
    private boolean win;

    public GameState(Player player, LevelManager gameLevel) {
        if (player == null) throw new IllegalArgumentException("Il giocatore non può essere nullo");
        if (gameLevel == null) throw new IllegalArgumentException("La partita deve avere almeno un livello");
        this.player = player;
        this.gameLevel = gameLevel;
        this.gameEnd = false;
        this.win = false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public LevelManager getGameLevel() {
        return this.gameLevel;
    }

    public Level getCurrentLevel() {
        return gameLevel.getCurrentLevel();
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

    public void EndGameVictory() {
        if (gameEnd) throw new IllegalStateException("La partita è già terminata");
        if (!gameLevel.lastLevel() || !getCurrentLevel().isCompleted()) throw new IllegalStateException("L'ultimo livello deve essere completato per vincere ");
        this.win = true;
        this.gameEnd = true;
    }

    public void nextLevel() {
        if (gameEnd) throw new IllegalStateException("La partita è già terminata");
        if (!gameLevel.nextLevel() && gameLevel.lastLevel() && getCurrentLevel().isCompleted()) {
            EndGameVictory();
        } else if (!gameLevel.nextLevel()) throw new IllegalStateException("Non è possibile avanzare al prossimo livello");
    }
}
