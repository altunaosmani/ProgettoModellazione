package it.unicam.cs.mpgc.rpg130579.model.interaction;

/**
 * Opzione di risposta selezionabile dal giocatore al termine delle battute di un nodo.
 * Può essere vincolata a una {@link QuestCondition} e attivare un {@link DialogueEffect}.
 *
 * @param text        testo della scelta mostrato al giocatore
 * @param nextNodeId  ID del nodo successivo, oppure {@code null} se la scelta termina il dialogo
 * @param effect      effetto collaterale eseguito alla selezione
 * @param requirement condizione necessaria affinché la scelta sia visibile
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