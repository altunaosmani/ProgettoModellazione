package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.view.ActionResult;

/**
 * Operazioni di salvataggio, caricamento e avvio di una nuova partita.
 */
public interface PersistenceController {
    /** @return {@code true} se esiste un salvataggio valido disponibile */
    boolean hasSave();

    /** @return esito del salvataggio dello stato corrente */
    ActionResult save();

    /** @return esito del caricamento dell'ultimo salvataggio disponibile */
    ActionResult load();

    /** Reinizializza il gioco avviando una nuova partita dall'inizio. */
    void newGame();
}
