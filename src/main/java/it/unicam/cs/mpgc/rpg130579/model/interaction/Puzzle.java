package it.unicam.cs.mpgc.rpg130579.model.interaction;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;

public interface Puzzle {
    String getId();

    String getDescription();

    boolean isSolvedBy(PuzzleAnswer answer, Player player);
}
