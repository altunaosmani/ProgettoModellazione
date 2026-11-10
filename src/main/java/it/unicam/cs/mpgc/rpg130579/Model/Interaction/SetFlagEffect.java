package it.unicam.cs.mpgc.rpg130579.Model.Interaction;

import it.unicam.cs.mpgc.rpg130579.Model.State.StoryProgress;

public record SetFlagEffect(String key, String value) implements DialogueEffect {
    public SetFlagEffect {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("La chiave del flag non può essere vuota");
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Il valore del flag non può essere vuoto");
    }

     @Override
    public void apply(StoryProgress progress) {
        if (progress == null) throw new IllegalArgumentException("Lo stato della storia non può essere nullo");
        progress.setFlag(key, value);
    }
}
