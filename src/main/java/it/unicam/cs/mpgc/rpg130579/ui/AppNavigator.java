package it.unicam.cs.mpgc.rpg130579.ui;

/**
 * Interfaccia di navigazione globale invocabile dalle singole schermate e dalla barra dei menu.
 */
public interface AppNavigator {
    void showMainMenu();

    /**
     * Avvia subito una nuova partita e mostra il prologo.
     */
    void startNewGame();

    /**
     * Come startNewGame, ma chiede prima conferma (i progressi non salvati andrebbero persi).
     */
    void requestNewGame();

    void loadGame();

    void saveGame();

    /**
     * Mostra la schermata di gioco.
     */
    void enterGame();

    void showEnding();

    void quit();
}