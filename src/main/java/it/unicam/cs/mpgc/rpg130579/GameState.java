package it.unicam.cs.mpgc.rpg130579;

public class GameState {
    private final Player player;
    private final GestoreLivelli gameLevel;
    private boolean partitaTerminata;
    private boolean vittoria;

    public GameState(Player player, GestoreLivelli gameLevel) {
        if (player == null) throw new IllegalArgumentException("Il giocatore non può essere nullo");
        if (gameLevel == null) throw new IllegalArgumentException("La partita deve avere almeno un livello");
        this.player = player;
        this.gameLevel = gameLevel;
        this.partitaTerminata = false;
        this.vittoria = false;
    }

    public Player getGiocatore() {
        return this.player;
    }

    public GestoreLivelli getGameLevel() {
        return this.gameLevel;
    }

    public Level getCurrentLevel() {
        return gameLevel.getCurrentLevel();
    }

    public boolean isPartitaTerminata() {
        return this.partitaTerminata;
    }

    public boolean isVittoria() {
        return this.vittoria;
    }

    public void endGame() {
        this.partitaTerminata = true;
    }

    public void EndGameVictory() {
        if (partitaTerminata) throw new IllegalStateException("La partita è già terminata");
        if (!gameLevel.lastLevel() || !getCurrentLevel().isCompleted()) throw new IllegalStateException("L'ultimo livello deve essere completato per vincere ");
        this.vittoria = true;
        this.partitaTerminata = true;
    }

    public void nextLevel() {
        if (partitaTerminata) throw new IllegalStateException("La partita è già terminata");
        if (!gameLevel.nextLevel() && gameLevel.lastLevel() && getCurrentLevel().isCompleted()) {
            EndGameVictory();
        } else if (!gameLevel.nextLevel()) throw new IllegalStateException("Non è possibile avanzare al prossimo livello");
    }
}
