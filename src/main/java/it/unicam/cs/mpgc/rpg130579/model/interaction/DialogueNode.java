package it.unicam.cs.mpgc.rpg130579.model.interaction;

import java.util.List;

public class DialogueNode {
    private final String id;
    private final List<String> lines;
    private final List<DialogueChoice> choices;

    public DialogueNode(String id, List<String> lines, List<DialogueChoice> choices) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID del nodo non può essere vuoto");
        if (lines == null || lines.isEmpty()) throw new IllegalArgumentException("Il nodo deve contenere almeno una battuta");
        for (String line : lines) {
            if (line == null || line.isBlank()) throw new IllegalArgumentException("Le battute non possono essere vuote");
        }
        if (choices == null) throw new IllegalArgumentException("La lista delle scelte non può essere nulla");
        for (DialogueChoice choice : choices) {
            if (choice == null) throw new IllegalArgumentException("La scelta non può essere nulla");
        }
        this.id = id;
        this.lines = List.copyOf(lines);
        this.choices = List.copyOf(choices);
    }

    public DialogueNode(String id, List<String> lines) {
        this(id, lines, List.of());
    }

    public String getId() {
        return id;
    }

    public List<String> getLines() {
        return lines;
    }

    public List<DialogueChoice> getChoices() {
        return choices;
    }

    public boolean hasChoices() {
        return !choices.isEmpty();
    }
}
