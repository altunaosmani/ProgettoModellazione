package it.unicam.cs.mpgc.rpg130579.view;

import java.util.Map;

public record PuzzleView(String description, Map<String, String> choices, int answerSize) {
    public PuzzleView {
        choices = Map.copyOf(choices);
    }
}
