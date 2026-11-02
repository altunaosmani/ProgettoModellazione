package it.unicam.cs.mpgc.rpg130579.Model.Interaction;

import java.util.List;
import java.util.Optional;

public record PuzzleAnswer(List<String> values) {
    public PuzzleAnswer {
        if (values == null) values = List.of();
        else for (String v : values) {
            if (v == null || v.isBlank()) throw new IllegalArgumentException("I valori della risposta non possono essere vuoti");
        }
        values = List.copyOf(values);
    }

    public static PuzzleAnswer of(String values) {
        return new PuzzleAnswer(List.of(values));
    }

    public static PuzzleAnswer empty() {
        return new PuzzleAnswer(List.of());
    }

    public Optional<String> first() {
        return values.isEmpty() ? Optional.empty() : Optional.of(values.get(0));
    }
}
