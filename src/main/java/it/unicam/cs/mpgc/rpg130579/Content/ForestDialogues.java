package it.unicam.cs.mpgc.rpg130579.Content;

import it.unicam.cs.mpgc.rpg130579.Model.Interaction.Dialogue;

import java.util.List;

public final class ForestDialogues {
    static Dialogue orwen() {
        return new Dialogue("Orwen", List.of(
                "Quel simbolo... lo conosco. Tua madre non era una semplice abitante di Tanusha.",
                "L'ultima volta che l'ho vista stava andando verso la Foresta dei Sussurri."));
    }

    private ForestDialogues() {
    }
}
