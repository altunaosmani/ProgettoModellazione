package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Enigma a risposta multipla in cui il giocatore deve selezionare l'unica opzione corretta.
 */
public class ChoicePuzzle extends AbstractPuzzle {
    private final Map<String, String> options;
    private final String correctOptionId;

    public ChoicePuzzle(String id, String description, Map<String, String> options, String correctOptionId) {
        super(id, description);
        if (options == null || options.size() < 2) throw new IllegalArgumentException("Occorrono almeno 2 opzioni");
        if (correctOptionId == null || correctOptionId.isBlank())
            throw new IllegalArgumentException("L'opzione corretta non può essere vuota");
        boolean present = options.keySet().stream().anyMatch(k -> k.equalsIgnoreCase(correctOptionId.trim()));
        if (!present) throw new IllegalArgumentException("L'opzione corretta deve essere tra quelle disponibili");
        this.options = Collections.unmodifiableMap(new LinkedHashMap<>(options));
        this.correctOptionId = correctOptionId.trim();
    }

    public Map<String, String> getOptions() {
        return this.options;
    }

    @Override
    public boolean isSolvedBy(PuzzleAnswer answer, Player player) {
        if (answer == null || answer.values().size() != 1) return false;
        return correctOptionId.equalsIgnoreCase(answer.values().get(0).trim());
    }

    @Override
    public int answerSize() {
        return 1;
    }

    @Override
    public Map<String, String> choices(Player player) {
        return options;
    }
}
