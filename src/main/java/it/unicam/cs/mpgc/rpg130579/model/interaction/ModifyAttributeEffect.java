package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;

/** Effetto che incrementa permanentemente un {@link Attribute} del protagonista. */public record ModifyAttributeEffect(Attribute attribute, int amount) implements DialogueEffect {
    public ModifyAttributeEffect {
        if (attribute == null) throw new IllegalArgumentException("L'attributo non può essere nullo");
        if (amount <= 0) throw new IllegalArgumentException("L'incremento deve essere positivo");
    }

    @Override
    public void apply(DialogueContext context) {
        if (context == null) throw new IllegalArgumentException("Il contesto non può essere nullo");
        context.player().getStats().increase(attribute, amount);
    }
}