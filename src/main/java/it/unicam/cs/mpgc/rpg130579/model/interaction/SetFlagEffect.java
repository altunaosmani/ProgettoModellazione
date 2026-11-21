package it.unicam.cs.mpgc.rpg130579.model.interaction;

/**
 * Effetto che imposta una coppia chiave-valore nei flag narrativi di
 * {@link it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress}.
 */
public record SetFlagEffect(String key, String value) implements DialogueEffect {
    public SetFlagEffect {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("La chiave del flag non può essere vuota");
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Il valore del flag non può essere vuoto");
    }

    @Override
    public void apply(DialogueContext context) {
        if (context == null) throw new IllegalArgumentException("Il contesto non può essere nullo");
        context.progress().setFlag(key, value);
    }
}
