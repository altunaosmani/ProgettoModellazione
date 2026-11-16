package it.unicam.cs.mpgc.rpg130579.model.interaction;

@FunctionalInterface
public interface DialogueEffect {
    DialogueEffect NONE = context -> { };

    void apply(DialogueContext context);
}
