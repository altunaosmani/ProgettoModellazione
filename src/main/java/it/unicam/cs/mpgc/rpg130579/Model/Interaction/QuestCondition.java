package it.unicam.cs.mpgc.rpg130579.Model.Interaction;

import it.unicam.cs.mpgc.rpg130579.Model.Entity.Player;
import it.unicam.cs.mpgc.rpg130579.Model.State.StoryProgress;

@FunctionalInterface
public interface QuestCondition {
    QuestCondition ALWAYS = (player, progress) -> true;

    boolean isMetBy(Player player, StoryProgress progress);
}