package it.unicam.cs.mpgc.rpg130579.view;

import java.util.List;

public record DialogueView(String speaker, List<String> lines, List<String> choices, boolean finished) {
    public DialogueView {
        lines = List.copyOf(lines);
        choices = List.copyOf(choices);
    }

    /** Vista di errore: una sola riga con il messaggio, nessuna scelta, dialogo concluso. */
    public static DialogueView error(String message) {
        String text = (message == null || message.isBlank()) ? "Non è possibile parlare in questo momento." : message;
        return new DialogueView("Sistema", List.of(text), List.of(), true);
    }
}