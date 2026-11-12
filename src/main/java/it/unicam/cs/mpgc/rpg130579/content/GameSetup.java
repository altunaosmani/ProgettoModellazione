package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.controller.GameState;
import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.state.EndingScreen;

public record GameSetup(GameState state, ItemCatalog catalog, EndingScreen victoryEnding) {
    public GameSetup {
        if (state == null) throw new IllegalArgumentException("Lo stato di gioco non può essere nullo");
        if (catalog == null) throw new IllegalArgumentException("Il catalogo non può essere nullo");
        if (victoryEnding == null) throw new IllegalArgumentException("Il finale di vittoria non può essere nullo");
    }
}
