package it.unicam.cs.mpgc.rpg130579.view;

import java.util.List;

public record DialogueView(String speaker, List<String> lines, List<String> choices, boolean finished) {
    public DialogueView {
        lines = List.copyOf(lines);
        choices = List.copyOf(choices);
    }
}