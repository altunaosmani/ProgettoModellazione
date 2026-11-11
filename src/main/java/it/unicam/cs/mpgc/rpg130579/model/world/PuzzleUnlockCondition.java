package it.unicam.cs.mpgc.rpg130579.model.world;

import it.unicam.cs.mpgc.rpg130579.model.interaction.Puzzle;

import java.util.Objects;
import java.util.Optional;

public class PuzzleUnlockCondition implements UnlockCondition {
    private final Puzzle puzzle;

    public PuzzleUnlockCondition(Puzzle puzzle) {
        this.puzzle = Objects.requireNonNull(puzzle, "L'enigma non può essere nullo");
    }

    public Puzzle getPuzzle() {
        return this.puzzle;
    }

    @Override
    public boolean isSatisfiedBy(UnlockAttempt attempt) {
        return puzzle.isSolvedBy(attempt.answer(), attempt.player());
    }

    @Override
    public Optional<Puzzle> requiredPuzzle() {
        return Optional.of(puzzle);
    }
}
