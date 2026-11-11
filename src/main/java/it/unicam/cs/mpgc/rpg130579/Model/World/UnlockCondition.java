package it.unicam.cs.mpgc.rpg130579.Model.World;

import it.unicam.cs.mpgc.rpg130579.Model.Interaction.Puzzle;

import java.util.Optional;

public interface UnlockCondition {
    boolean isSatisfiedBy(UnlockAttempt attempt);

    default Optional<Puzzle> requiredPuzzle() { return Optional.empty(); }
}
