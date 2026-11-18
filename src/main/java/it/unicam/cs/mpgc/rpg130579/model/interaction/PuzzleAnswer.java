package it.unicam.cs.mpgc.rpg130579.model.interaction;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Incapsula la risposta fornita dal giocatore a un {@link Puzzle} come lista immutabile di valori.
 *
 * @param values lista ordinata degli identificativi o valori scelti dal giocatore
 */
public record PuzzleAnswer(List<String> values) {
    public PuzzleAnswer {
        if (values == null) {
            values = List.of();
        } else {
            for (String v : values) {
                if (v == null || v.isBlank()) throw new IllegalArgumentException("I valori della risposta non possono essere vuoti");
            }
            values = List.copyOf(values);
        }
    }

    public static PuzzleAnswer of(String... values) {
        return new PuzzleAnswer(values == null ? List.of() : Arrays.asList(values));
    }

    public static PuzzleAnswer empty() {
        return new PuzzleAnswer(List.of());
    }

    public Optional<String> first() {
        return values.isEmpty() ? Optional.empty() : Optional.of(values.get(0));
    }
}
