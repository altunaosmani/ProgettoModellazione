package it.unicam.cs.mpgc.rpg130579.model.interaction;

/** Azione collaterale eseguita quando il giocatore seleziona una {@link DialogueChoice}. */
@FunctionalInterface
public interface DialogueEffect {
    DialogueEffect NONE = context -> { };

    void apply(DialogueContext context);
}
