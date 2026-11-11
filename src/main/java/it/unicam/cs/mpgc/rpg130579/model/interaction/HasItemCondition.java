package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;

public record HasItemCondition(String itemId) implements QuestCondition {
    public HasItemCondition {
        if (itemId == null || itemId.isBlank()) throw new IllegalArgumentException("L'ID dell'oggetto non può essere vuoto");
    }

    @Override
    public boolean isMetBy(Player player, StoryProgress progress) {
        return player.hasItem(itemId);
    }
}
