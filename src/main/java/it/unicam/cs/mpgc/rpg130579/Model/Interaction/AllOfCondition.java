package it.unicam.cs.mpgc.rpg130579.Model.Interaction;

import it.unicam.cs.mpgc.rpg130579.Model.Entity.Player;
import it.unicam.cs.mpgc.rpg130579.Model.State.StoryProgress;

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
