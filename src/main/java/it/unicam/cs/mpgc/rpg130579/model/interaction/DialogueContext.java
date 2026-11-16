package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;

/** Ciò che un dialogo può leggere e modificare: il giocatore e i progressi della storia. */
public record DialogueContext(Player player, StoryProgress progress) {
    public DialogueContext {
        if (player == null) throw new IllegalArgumentException("Il giocatore non può essere nullo");
        if (progress == null) throw new IllegalArgumentException("Lo stato della storia non può essere nullo");
    }
}
