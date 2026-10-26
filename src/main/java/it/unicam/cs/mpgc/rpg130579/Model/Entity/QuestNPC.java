package it.unicam.cs.mpgc.rpg130579.Model.Entity;

import it.unicam.cs.mpgc.rpg130579.Model.Interaction.Dialogue;
import it.unicam.cs.mpgc.rpg130579.Model.World.Location;

public class QuestNPC extends NPC {
    private final String requiredItemId;
    private final Dialogue conditionalDialogue;

    public QuestNPC(String id, String name, String description, Location currentLocation, Dialogue defaultDialogue, String requiredItemId, Dialogue conditionalDialogue) {
        super(id, name, description, currentLocation, defaultDialogue);
        if (requiredItemId == null || requiredItemId.isBlank()) throw new IllegalArgumentException("L'ID dell'oggetto richeisto non può essere vuoto");
        if (conditionalDialogue == null) throw new IllegalArgumentException("Il dialogo in questione non può essere nullo");
        this.requiredItemId = requiredItemId;
        this.conditionalDialogue = conditionalDialogue;
    }

    public String getRequiredItemId() {
        return requiredItemId;
    }

    public Dialogue getConditionalDialogue() {
        return conditionalDialogue;
    }

    @Override
    public Dialogue interact(Player player) {
        if (player != null && player.hasItem(requiredItemId)) return conditionalDialogue;
        return super.getDialogue();
    }
}
