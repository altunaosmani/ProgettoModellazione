package it.unicam.cs.mpgc.rpg130579.Model.World;

import it.unicam.cs.mpgc.rpg130579.Model.Entity.Player;
import it.unicam.cs.mpgc.rpg130579.Model.Interaction.Puzzle;
import it.unicam.cs.mpgc.rpg130579.Model.Interaction.PuzzleAnswer;

import java.util.Optional;

public class Mechanism implements Lockable {
    private final String id;
    private final String name;
    private final String description;
    private final UnlockCondition unlockCondition;
    private boolean locked;

    public Mechanism(String id, String name, String description, UnlockCondition unlockCOndition) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID del meccanismo non può essere vuoto");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Il nome del meccanismo non può essere vuoto");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("La descriizone del meccanismo non può essere vuota");
        if (unlockCOndition == null) throw new IllegalArgumentException("La condizione di sblocco non può essere nulla");
        this. id = id;
        this.name = name;
        this.description = description;
        this.unlockCondition = unlockCOndition;
        this.locked = true;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    public boolean tryUnlock(Player player, PuzzleAnswer answer) {
        if (player == null) throw new IllegalArgumentException("Il giocatore non può essere nullo");
        if (!locked) return true;
        if (unlockCondition.isSatisfiedBy(new UnlockAttempt(id, player, answer))) locked = false;
        return !locked;
    }

    public boolean tryUnlock(Player player) {
        return tryUnlock(player, PuzzleAnswer.empty());
    }

    public void restoreUnlocked() {
        this.locked = false;
    }

    public Optional<Puzzle> getRequiredPuzzle() {
        return unlockCondition.requiredPuzzle();
    }
}
