package it.unicam.cs.mpgc.rpg130579.Model.World;

import it.unicam.cs.mpgc.rpg130579.Model.Interaction.Puzzle;

import java.util.Objects;

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
}
