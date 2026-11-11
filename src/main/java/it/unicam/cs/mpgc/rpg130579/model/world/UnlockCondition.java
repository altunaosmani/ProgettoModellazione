package it.unicam.cs.mpgc.rpg130579.model.world;

import it.unicam.cs.mpgc.rpg130579.model.interaction.Puzzle;

import java.util.Optional;

public interface UnlockCondition {
    boolean isSatisfiedBy(UnlockAttempt attempt);

    default Optional<Puzzle> requiredPuzzle() { return Optional.empty(); }
}
