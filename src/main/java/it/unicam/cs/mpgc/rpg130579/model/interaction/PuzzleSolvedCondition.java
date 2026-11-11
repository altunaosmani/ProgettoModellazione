package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;

public record PuzzleSolvedCondition(String puzzleId) implements QuestCondition {
    public PuzzleSolvedCondition {
        if (puzzleId == null || puzzleId.isBlank()) throw new IllegalArgumentException("L'ID dell'enigma non può essere vuoto");
    }

    @Override
    public boolean isMetBy(Player player, StoryProgress progress) {
        return progress.isPuzzleSolved(puzzleId);
    }
}
