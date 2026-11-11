package it.unicam.cs.mpgc.rpg130579.model.interaction;

public record DialogueChoice(String text, String nextNodeId, DialogueEffect effect) {
    public DialogueChoice {
        if (text == null || text.isBlank()) throw new IllegalArgumentException("Il testo della scelta non può essere vuoto");
        if (nextNodeId != null && nextNodeId.isBlank()) throw new IllegalArgumentException("L'ID del nodo successivo non può essere vuoto");
        if (effect == null) effect = DialogueEffect.NONE;
    }

    public DialogueChoice(String text, String nextNodeId) {
        this(text, nextNodeId, DialogueEffect.NONE);
    }

    public boolean endsDialogue() {
        return nextNodeId == null;
    }
}
