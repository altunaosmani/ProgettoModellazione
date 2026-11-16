package it.unicam.cs.mpgc.rpg130579.model.interaction;

import java.util.List;

/** Applica più effetti in sequenza (es. impostare un flag e aumentare un attributo). */
public record CompositeEffect(List<DialogueEffect> effects) implements DialogueEffect {
    public CompositeEffect {
        if (effects == null || effects.isEmpty()) throw new IllegalArgumentException("Serve almeno un effetto");
        for (DialogueEffect effect : effects) {
            if (effect == null) throw new IllegalArgumentException("L'effetto non può essere nullo");
        }
        effects = List.copyOf(effects);
    }

    public static CompositeEffect of(DialogueEffect... effects) {
        return new CompositeEffect(List.of(effects));
    }

    @Override
    public void apply(DialogueContext context) {
        effects.forEach(effect -> effect.apply(context));
    }
}