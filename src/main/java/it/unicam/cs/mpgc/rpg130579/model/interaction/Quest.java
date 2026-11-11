package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;

public class Quest {
    private final String id;
    private final String name;
    private final String description;
    private boolean completed;
    private final QuestCondition condition;

    public Quest(String id, String name, String description, QuestCondition condition) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID della missione non può essere vuoto");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Il nome della missione non può essere vuoto");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("La descrizione della missione non può essere vuota");
        if (condition == null) throw new IllegalArgumentException("La condizione della missione non può essere nulla");
        this.id = id;
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.completed = false;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void complete() {
        this.completed = true;
    }

    public boolean isCompletable(Player player, StoryProgress progress) {
        return !completed && condition.isMetBy(player, progress);
    }
}
