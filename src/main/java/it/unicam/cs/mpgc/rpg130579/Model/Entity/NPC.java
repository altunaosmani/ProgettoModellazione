package it.unicam.cs.mpgc.rpg130579.Model.Entity;

import it.unicam.cs.mpgc.rpg130579.Model.Interaction.Dialogue;
import it.unicam.cs.mpgc.rpg130579.Model.World.Location;

public class NPC extends Character {
    private Dialogue dialogue;

    public NPC(String name, String id, String description, Location currentLocation, Dialogue dialogue) {
        super(id, name, description, currentLocation);
        if (dialogue == null) throw new IllegalArgumentException("Ogni personaggio deve avere un dialogo");
        this.dialogue = dialogue;
    }

    public Dialogue getDialogue() {
        return this.dialogue;
    }

    public void setDialogue(Dialogue dialogue) {
        if (dialogue == null) throw new IllegalArgumentException("Il dialogo non può essere nullo");
        this.dialogue = dialogue;
    }

    public Dialogue interact(Player player) {
        return this.dialogue;
    }
}
