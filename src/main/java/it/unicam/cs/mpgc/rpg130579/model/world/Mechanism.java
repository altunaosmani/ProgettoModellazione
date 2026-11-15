package it.unicam.cs.mpgc.rpg130579.model.world;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.interaction.Puzzle;
import it.unicam.cs.mpgc.rpg130579.model.interaction.PuzzleAnswer;

import java.util.Optional;

/** Elemento del mondo che parte bloccato e si apre quando la sua UnlockCondition è soddisfatta. */
public class Mechanism implements Lockable {
    private final String id;
    private final String name;
    private final String description;
    private final UnlockCondition unlockCondition;
    private boolean locked;

    public Mechanism(String id, String name, String description, UnlockCondition unlockCondition) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID del meccanismo non può essere vuoto");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Il nome del meccanismo non può essere vuoto");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("La descrizione del meccanismo non può essere vuota");
        if (unlockCondition == null) throw new IllegalArgumentException("La condizione di sblocco non può essere nulla");
        this.id = id;
        this.name = name;
        this.description = description;
        this.unlockCondition = unlockCondition;
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

    /** Usato dal caricamento di un salvataggio: apre senza controllare le condizioni. */
    public void restoreUnlocked() {
        this.locked = false;
    }

    public Optional<Puzzle> getRequiredPuzzle() {
        return unlockCondition.requiredPuzzle();
    }
}
