package it.unicam.cs.mpgc.rpg130579.model.entity;

import it.unicam.cs.mpgc.rpg130579.model.interaction.Dialogue;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;

/**
 * Personaggio non giocante (NPC) presente in un luogo del mondo con cui il giocatore può dialogare.
 */
public class NPC extends GameCharacter {
    private Dialogue dialogue;

    public NPC(String id, String name, String description, Location currentLocation, Dialogue dialogue) {
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

    /**
     * Seleziona il dialogo da avviare con il giocatore.
     *
     * @param player   il protagonista
     * @param progress stato corrente dei progressi della storia
     * @return il {@link Dialogue} da eseguire
     */
    public Dialogue interact(Player player, StoryProgress progress) {
        return this.dialogue;
    }
}
