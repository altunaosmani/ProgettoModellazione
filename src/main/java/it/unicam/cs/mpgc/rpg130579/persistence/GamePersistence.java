package it.unicam.cs.mpgc.rpg130579.persistence;

import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.state.GameState;

/** Cosa serve al controller per salvare e caricare: non sa nulla di JSON, file o SaveData. */
public interface GamePersistence {
    /** Vero se esiste un salvataggio leggibile. */
    boolean hasSave();

    /** @throws java.io.UncheckedIOException se la scrittura fallisce */
    void save(GameState state);

    /**
     * Ripristina lo stato indicato (appena costruito dal contenuto di gioco).
     *
     * @return false se non esiste nessun salvataggio
     * @throws IllegalStateException    se il salvataggio non è coerente col contenuto di gioco;
     *                                  in tal caso lo stato non viene modificato
     * @throws java.io.UncheckedIOException se il file non è leggibile
     */
    boolean load(GameState state, ItemCatalog catalog);
}