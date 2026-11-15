package it.unicam.cs.mpgc.rpg130579.model.state;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;
import it.unicam.cs.mpgc.rpg130579.model.world.Level;
import it.unicam.cs.mpgc.rpg130579.model.world.LevelManager;

/** Stato completo di una partita: giocatore, livelli, progressi della storia ed esito finale. */
public class GameState {
    private final Player player;
    private final LevelManager levelManager;
    private final StoryProgress progress;
    private boolean gameEnd;
    private boolean win;

    public GameState(Player player, LevelManager levelManager) {
        if (player == null) throw new IllegalArgumentException("Il giocatore non può essere nullo");
        if (levelManager == null) throw new IllegalArgumentException("La partita deve avere almeno un livello");
        this.player = player;
        this.levelManager = levelManager;
        this.progress = new StoryProgress();
        this.gameEnd = false;
        this.win = false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public LevelManager getLevelManager() {
        return this.levelManager;
    }

    public int getCurrentLevelIndex() {
        return this.levelManager.getIndexCurrentLevel();
    }

    public Level getCurrentLevel() {
        return levelManager.getCurrentLevel();
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

    public void endGameVictory() {
        if (gameEnd) throw new IllegalStateException("La partita è già terminata");
        if (!levelManager.isLastLevel() || !getCurrentLevel().isCompleted())
            throw new IllegalStateException("L'ultimo livello deve essere completato per vincere");
        this.win = true;
        this.gameEnd = true;
    }

    /** Usato dal caricamento di un salvataggio di una partita già conclusa. */
    public void restoreEnded(boolean won) {
        this.gameEnd = true;
        this.win = won;
    }

    /** Avanza al livello successivo; se il livello completato è l'ultimo, la partita si chiude con la vittoria. */
    public void nextLevel() {
        if (gameEnd) throw new IllegalStateException("La partita è già terminata");
        if (levelManager.nextLevel()) {
            player.setCurrentLocation(getCurrentLevel().getStartLocation());
            return;
        }
        if (levelManager.isLastLevel() && getCurrentLevel().isCompleted()) {
            endGameVictory();
            return;
        }
        throw new IllegalStateException("Non è possibile avanzare al prossimo livello");
    }
}
