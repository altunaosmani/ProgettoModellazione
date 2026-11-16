package it.unicam.cs.mpgc.rpg130579.model.interaction;

/**
 * Una scelta del giocatore. Se nextNodeId è null chiude il dialogo.
 * La scelta è visibile solo se il requisito è soddisfatto.
 */
public record DialogueChoice(String text, String nextNodeId, DialogueEffect effect, QuestCondition requirement) {
    public DialogueChoice {
        if (text == null || text.isBlank()) throw new IllegalArgumentException("Il testo della scelta non può essere vuoto");
        if (nextNodeId != null && nextNodeId.isBlank()) throw new IllegalArgumentException("L'ID del nodo successivo non può essere vuoto");
        if (effect == null) effect = DialogueEffect.NONE;
        if (requirement == null) requirement = QuestCondition.ALWAYS;
    }

    public DialogueChoice(String text, String nextNodeId, DialogueEffect effect) {
        this(text, nextNodeId, effect, QuestCondition.ALWAYS);
    }

    public DialogueChoice(String text, String nextNodeId) {
        this(text, nextNodeId, DialogueEffect.NONE, QuestCondition.ALWAYS);
    }

    public boolean endsDialogue() {
        return nextNodeId == null;
    }

    public boolean isUnconditional() {
        return requirement == QuestCondition.ALWAYS;
    }
}