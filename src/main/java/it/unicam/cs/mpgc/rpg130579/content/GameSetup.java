package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.state.EndingCatalog;
import it.unicam.cs.mpgc.rpg130579.model.state.GameState;
import it.unicam.cs.mpgc.rpg130579.model.state.IntroScreen;

/**
 * Contenitore immutabile di tutti gli elementi necessari per avviare o ripristinare una partita.
 *
 * @param state       stato iniziale del gioco (giocatore, livelli e progressi)
 * @param catalog     registro globale di tutti gli oggetti esistenti nel gioco
 * @param introScreen schermata introduttiva (prologo) della storia
 * @param endings     catalogo dei finali disponibili e delle relative condizioni di attivazione
 */
public record GameSetup(GameState state, ItemCatalog catalog, IntroScreen introScreen, EndingCatalog endings) {
    public GameSetup {
        if (state == null) throw new IllegalArgumentException("Lo stato di gioco non può essere nullo");
        if (catalog == null) throw new IllegalArgumentException("Il catalogo non può essere nullo");
        if (introScreen == null) throw new IllegalArgumentException("L'introduzione non può essere nulla");
        if (endings == null) throw new IllegalArgumentException("I finali non possono essere nulli");
    }
}