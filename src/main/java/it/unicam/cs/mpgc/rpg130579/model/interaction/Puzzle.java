package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;

import java.util.Map;

/**
 * Rappresenta un enigma associato a un meccanismo del mondo di gioco.
 */
public interface Puzzle {
    /**
     * @return identificativo univoco dell'enigma
     */
    String getId();

    /**
     * @return descrizione o domanda posta al giocatore
     */
    String getDescription();

    /**
     * Verifica se la risposta fornita e lo stato del giocatore risolvono l'enigma.
     *
     * @param answer risposta sottomessa dal giocatore
     * @param player protagonista (necessario per verificare il possesso di oggetti)
     * @return {@code true} se l'enigma è risolto
     */
    boolean isSolvedBy(PuzzleAnswer answer, Player player);

    /**
     * @return numero di elementi attesi nella risposta ({@code 0} se non richiede input esplicito)
     */
    default int answerSize() {
        return 0;
    }

    /**
     * Restituisce le opzioni selezionabili dal giocatore (mappa ID opzione -> etichetta).
     *
     * @param player protagonista corrente
     * @return mappa ordinata delle opzioni disponibili
     */
    default Map<String, String> choices(Player player) {
        return Map.of();
    }
}
