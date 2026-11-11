package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;

@FunctionalInterface
public interface QuestCondition {
    QuestCondition ALWAYS = (player, progress) -> true;

    boolean isMetBy(Player player, StoryProgress progress);
}