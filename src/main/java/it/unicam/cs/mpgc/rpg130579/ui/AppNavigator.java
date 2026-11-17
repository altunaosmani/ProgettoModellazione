package it.unicam.cs.mpgc.rpg130579.ui;

/** Le transizioni tra schermate e le azioni globali che le singole schermate possono richiedere. */
public interface AppNavigator {
    void showMainMenu();

    /** Avvia subito una nuova partita e mostra il prologo. */
    void startNewGame();

    /** Come startNewGame, ma chiede prima conferma (i progressi non salvati andrebbero persi). */
    void requestNewGame();

    void loadGame();

    void saveGame();

    /** Mostra la schermata di gioco. */
    void enterGame();

    void showEnding();

    void quit();
}