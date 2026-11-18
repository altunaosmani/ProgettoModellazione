package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;

/**
 * Condizione verificata se un flag narrativo possiede il valore atteso (case-insensitive).
 */
public record FlagCondition(String key, String value) implements QuestCondition {
    public FlagCondition {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("La chiave del flag non può essere vuota");
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Il valore del flag non può essere vuoto");
    }

    @Override
    public boolean isMetBy(Player player, StoryProgress progress) {
        return progress.hasFlag(key, value);
    }
}
