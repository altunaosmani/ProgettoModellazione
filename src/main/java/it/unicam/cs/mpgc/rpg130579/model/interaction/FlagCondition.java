package it.unicam.cs.mpgc.rpg130579.model.interaction;


import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;

public record FlagCondition(String key, String value) implements QuestCondition {
    @Override
    public boolean isMetBy(Player player, StoryProgress progress) {
        return progress.hasFlag(key, value);
    }
}
