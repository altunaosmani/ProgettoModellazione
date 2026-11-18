package it.unicam.cs.mpgc.rpg130579.model.interaction;

import java.util.List;

/**
 * Macchina a stati che governa l'avanzamento di una singola conversazione con un NPC.
 * Gestisce la lettura sequenziale delle battute, il filtraggio delle scelte disponibili
 * e la transizione tra i nodi fino alla chiusura del dialogo.
 */
public class DialogueSession {
    private final Dialogue dialogue;
    private final DialogueContext context;
    private DialogueNode currentNode;
    private int lineIndex;
    private boolean closed;

    public DialogueSession(Dialogue dialogue, DialogueContext context) {
        if (dialogue == null) throw new IllegalArgumentException("Il dialogo non può essere nullo");
        if (context == null) throw new IllegalArgumentException("Il contesto del dialogo non può essere nullo");
        this.dialogue = dialogue;
        this.context = context;
        this.currentNode = dialogue.getStartNode();
        this.lineIndex = 0;
        this.closed = false;
    }

    public String getTitle() {
        return dialogue.getTitle();
    }

    public boolean hasNextLine() {
        return !closed && lineIndex < currentNode.getLines().size();
    }

    public String nextLine() {
        if (!hasNextLine()) throw new IllegalStateException("Non ci sono altre battute");
        return currentNode.getLines().get(lineIndex++);
    }

    /** Vero quando le battute sono finite e il giocatore deve scegliere. */
    public boolean awaitsChoice() {
        return !closed && !hasNextLine() && !availableChoices().isEmpty();
    }

    /** Solo le scelte il cui requisito è soddisfatto; gli indici di choose() si riferiscono a questa lista. */
    public List<DialogueChoice> getChoices() {
        if (!awaitsChoice()) throw new IllegalStateException("In questo momento non ci sono scelte da fare");
        return availableChoices();
    }

    public void choose(int index) {
        List<DialogueChoice> choices = getChoices();
        if (index < 0 || index >= choices.size()) throw new IllegalArgumentException("Indice della scelta non valido");
        DialogueChoice choice = choices.get(index);
        choice.effect().apply(context);
        if (choice.endsDialogue()) {
            closed = true;
        } else {
            currentNode = dialogue.getNode(choice.nextNodeId());
            lineIndex = 0;
        }
    }

    public boolean isFinished() {
        return closed || (!hasNextLine() && availableChoices().isEmpty());
    }

    private List<DialogueChoice> availableChoices() {
        return currentNode.getChoices().stream()
                .filter(c -> c.requirement().isMetBy(context.player(), context.progress()))
                .toList();
    }
}