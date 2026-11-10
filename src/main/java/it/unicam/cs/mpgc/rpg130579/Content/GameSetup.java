package it.unicam.cs.mpgc.rpg130579.Content;

import it.unicam.cs.mpgc.rpg130579.Controller.GameState;
import it.unicam.cs.mpgc.rpg130579.Model.Entity.ItemCatalog;

public record GameSetup (GameState state, ItemCatalog catalog) {
    public GameSetup {
        if (state == null) throw new IllegalArgumentException("Lo stato di gioco non può essere nullo");
        if (catalog == null) throw new IllegalArgumentException("Il catalogo non può essere nullo");
    }
}
