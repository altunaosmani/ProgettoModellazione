package it.unicam.cs.mpgc.rpg130579.Model.Interaction;

import it.unicam.cs.mpgc.rpg130579.Model.State.StoryProgress;

import java.util.List;

public class DialogueSession {
    private final Dialogue dialogue;
    private final StoryProgress progress;
    private DialogueNode currentNode;
    private int lineIndex;
    private boolean closed;

    public DialogueSession(Dialogue dialogue, StoryProgress progress) {
        if (dialogue == null) throw new IllegalArgumentException("Il dialogo non può essere nullo");
        if (progress == null) throw new IllegalArgumentException("Lo stato della storia non può essere nullo");
        this.dialogue = dialogue;
        this.progress = progress;
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
        return !closed && !hasNextLine() && currentNode.hasChoices();
    }

    public List<DialogueChoice> getChoices() {
        if (!awaitsChoice()) throw new IllegalStateException("In questo momento non ci sono scelte da fare");
        return currentNode.getChoices();
    }

    public void choose(int index) {
        List<DialogueChoice> choices = getChoices();
        if (index < 0 || index >= choices.size()) throw new IllegalArgumentException("Indice della scelta non valido");
        DialogueChoice choice = choices.get(index);
        choice.effect().apply(progress);
        if (choice.endsDialogue()) {
            closed = true;
        } else {
            currentNode = dialogue.getNode(choice.nextNodeId());
            lineIndex = 0;
        }
    }

    public boolean isFinished() {
        return closed || (!hasNextLine() && !currentNode.hasChoices());
    }
}
