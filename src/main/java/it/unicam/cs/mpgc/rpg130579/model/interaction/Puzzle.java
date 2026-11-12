package it.unicam.cs.mpgc.rpg130579.model.interaction;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;

import java.util.Map;

public interface Puzzle {
    String getId();

    String getDescription();

    boolean isSolvedBy(PuzzleAnswer answer, Player player);

    default int answerSize() { return 0; }

    default Map<String, String> choices(Player player) { return Map.of(); }
}
