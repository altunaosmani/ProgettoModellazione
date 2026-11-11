package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;

import java.util.List;

public record AllOfCondition(List<QuestCondition> conditions) implements QuestCondition {
    public AllOfCondition {
        if (conditions == null || conditions.isEmpty()) throw new IllegalArgumentException("Serve almeno una condizione");
        conditions = List.copyOf(conditions);
    }

    @Override
    public boolean isMetBy(Player player, StoryProgress progress) {
        return conditions.stream().allMatch(c -> c.isMetBy(player, progress));
    }
}
