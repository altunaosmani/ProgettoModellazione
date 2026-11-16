package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.state.EndingCatalog;
import it.unicam.cs.mpgc.rpg130579.model.state.GameState;
import it.unicam.cs.mpgc.rpg130579.model.state.IntroScreen;

public record GameSetup(GameState state, ItemCatalog catalog, IntroScreen introScreen, EndingCatalog endings) {
    public GameSetup {
        if (state == null) throw new IllegalArgumentException("Lo stato di gioco non può essere nullo");
        if (catalog == null) throw new IllegalArgumentException("Il catalogo non può essere nullo");
        if (introScreen == null) throw new IllegalArgumentException("L'introduzione non può essere nulla");
        if (endings == null) throw new IllegalArgumentException("I finali non possono essere nulli");
    }
}