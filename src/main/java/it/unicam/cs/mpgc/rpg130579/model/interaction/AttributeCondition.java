package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;

/**
 * Condizione verificata se il valore effettivo di un attributo è almeno pari alla soglia {@code min}.
 */
public record AttributeCondition(Attribute attribute, int min) implements QuestCondition {
    public AttributeCondition {
        if (attribute == null) throw new IllegalArgumentException("L'attributo non può essere nullo");
        if (min < 0) throw new IllegalArgumentException("Il valore minimo non può essere negativo");
    }

    @Override
    public boolean isMetBy(Player player, StoryProgress progress) {
        return player.getAttributeValue(attribute) >= min;
    }
}