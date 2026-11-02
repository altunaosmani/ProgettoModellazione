package it.unicam.cs.mpgc.rpg130579.Model.Interaction;
import it.unicam.cs.mpgc.rpg130579.Model.Entity.Player;

public interface Puzzle {
    String getId();

    String getDescription();

    boolean isSolvedBy(PuzzleAnswer answer, Player player);
}
