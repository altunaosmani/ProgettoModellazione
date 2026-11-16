package it.unicam.cs.mpgc.rpg130579.model.world;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;

/** Bonus a un attributo assegnato la prima volta che un meccanismo viene sbloccato. */
public record AttributeReward(Attribute attribute, int amount) {
    public AttributeReward {
        if (attribute == null) throw new IllegalArgumentException("L'attributo non può essere nullo");
        if (amount <= 0) throw new IllegalArgumentException("Il bonus deve essere positivo");
    }
}