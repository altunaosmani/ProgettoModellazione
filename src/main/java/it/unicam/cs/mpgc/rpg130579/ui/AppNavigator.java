package it.unicam.cs.mpgc.rpg130579.ui;

/**
 * Interfaccia di navigazione globale invocabile dalle singole schermate e dalla barra dei menu.
 */
public interface AppNavigator {
    /**
     * Mostra la schermata del menu principale.
     */
    void showMainMenu();

    /**
     * Avvia subito una nuova partita e mostra il prologo.
     */
    void startNewGame();

    /**
     * Come {@link #startNewGame()}, ma chiede prima conferma all'utente per evitare la perdita
     * di progressi non salvati.
     */
    void requestNewGame();

    /**
     * Carica l'ultimo salvataggio disponibile ed entra nella schermata di gioco.
     */
    void loadGame();

    /**
     * Salva lo stato corrente della partita.
     */
    void saveGame();

    /**
     * Mostra la schermata principale di gioco.
     */
    void enterGame();

    /**
     * Mostra la schermata conclusiva con il finale raggiunto.
     */
    void showEnding();

    /**
     * Chiude l'applicazione.
     */
    void quit();
}