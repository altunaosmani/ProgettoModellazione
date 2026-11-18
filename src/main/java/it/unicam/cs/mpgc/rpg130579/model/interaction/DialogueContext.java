package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;

/**
 * Contesto passato agli effetti dei dialoghi, contenente i riferimenti al {@link Player}
 * e allo {@link StoryProgress} modificabili durante la conversazione.
 */
public record DialogueContext(Player player, StoryProgress progress) {
    public DialogueContext {
        if (player == null) throw new IllegalArgumentException("Il giocatore non può essere nullo");
        if (progress == null) throw new IllegalArgumentException("Lo stato della storia non può essere nullo");
    }
}
