package it.unicam.cs.mpgc.rpg130579.Model.Interaction;


import it.unicam.cs.mpgc.rpg130579.Model.Entity.Player;
import it.unicam.cs.mpgc.rpg130579.Model.State.StoryProgress;

public record FlagCondition(String key, String value) implements QuestCondition {
    @Override
    public boolean isMetBy(Player player, StoryProgress progress) {
        return progress.hasFlag(key, value);
    }
}
