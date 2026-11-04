package it.unicam.cs.mpgc.rpg130579.Model.Interaction;

public class DialogueSession {
    private final Dialogue dialogue;
    private int index;

    public DialogueSession(Dialogue dialogue) {
        if (dialogue == null) throw new IllegalArgumentException("Il dialogo non può essere nullo");
        this.dialogue = dialogue;
        this.index = 0;
    }

    public String getTitle() {
        return dialogue.getTitle();
    }

    public boolean hasNextLine() {
        return index < dialogue.getLinesSize();
    }

    public String nextLine() {
        if (!hasNextLine()) throw new IllegalArgumentException("Il dialogo è terminato");
        return dialogue.getLine(index++);
    }
}
