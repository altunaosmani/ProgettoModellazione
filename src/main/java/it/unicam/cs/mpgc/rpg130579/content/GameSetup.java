package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.state.GameState;
import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.state.EndingScreen;
import it.unicam.cs.mpgc.rpg130579.model.state.IntroScreen;

public record GameSetup(GameState state, ItemCatalog catalog, IntroScreen introScreen, EndingScreen victoryEnding) {
    public GameSetup {
        if (state == null) throw new IllegalArgumentException("Lo stato di gioco non può essere nullo");
        if (catalog == null) throw new IllegalArgumentException("Il catalogo non può essere nullo");
        if (introScreen == null) throw new IllegalArgumentException("L'introduzione non può essere nulla");
        if (victoryEnding == null) throw new IllegalArgumentException("Il finale di vittoria non può essere nullo");
    }
}
