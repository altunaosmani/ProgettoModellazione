package it.unicam.cs.mpgc.rpg130579.view;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Dati di presentazione di un enigma interattivo per la UI. */
public record PuzzleView(String description, Map<String, String> choices, int answerSize) {
    public PuzzleView {
        choices = Collections.unmodifiableMap(new LinkedHashMap<>(choices));
    }
}
