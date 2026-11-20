package it.unicam.cs.mpgc.rpg130579.model.entity;

import it.unicam.cs.mpgc.rpg130579.model.interaction.Dialogue;
import it.unicam.cs.mpgc.rpg130579.model.interaction.Condition;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;

/**
 * Specializzazione di {@link NPC} il cui dialogo cambia dinamicamente al verificarsi
 * di una {@link Condition} (ad esempio dopo aver completato una richiesta o impostato un flag).
 */
public class QuestNPC extends NPC {
    private final Condition condition;
    private final Dialogue conditionalDialogue;

    public QuestNPC(String id, String name, String description, Location currentLocation,
                    Dialogue defaultDialogue, Condition condition, Dialogue conditionalDialogue) {
        super(id, name, description, currentLocation, defaultDialogue);
        if (condition == null) throw new IllegalArgumentException("La condizione non può essere nulla");
        if (conditionalDialogue == null)
            throw new IllegalArgumentException("Il dialogo condizionale non può essere nullo");
        this.condition = condition;
        this.conditionalDialogue = conditionalDialogue;
    }

    public Dialogue getConditionalDialogue() {
        return conditionalDialogue;
    }

    @Override
    public Dialogue interact(Player player, StoryProgress progress) {
        return condition.isMetBy(player, progress) ? conditionalDialogue : getDialogue();
    }
}
