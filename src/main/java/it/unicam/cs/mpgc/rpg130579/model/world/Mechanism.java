package it.unicam.cs.mpgc.rpg130579.model.world;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.interaction.ConditionalText;
import it.unicam.cs.mpgc.rpg130579.model.interaction.Puzzle;
import it.unicam.cs.mpgc.rpg130579.model.interaction.PuzzleAnswer;
import it.unicam.cs.mpgc.rpg130579.model.interaction.Condition;

import java.util.Optional;

/**
 * Elemento interattivo di un luogo (cancello, statua, passaggio segreto) che nasce bloccato
 * e può essere aperto soddisfacendo la propria {@link UnlockCondition}.
 * Può offrire un indizio condizionale e assegnare una ricompensa in attributi al primo sblocco.
 */
public class Mechanism implements Lockable {
    private final String id;
    private final String name;
    private final String description;
    private final UnlockCondition unlockCondition;
    private boolean locked;
    private AttributeReward reward;
    private ConditionalText hint;

    public Mechanism(String id, String name, String description, UnlockCondition unlockCondition) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID del meccanismo non può essere vuoto");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Il nome del meccanismo non può essere vuoto");
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("La descrizione del meccanismo non può essere vuota");
        if (unlockCondition == null)
            throw new IllegalArgumentException("La condizione di sblocco non può essere nulla");
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

    /**
     * Imposta il bonus assegnato allo sblocco; restituisce this per l'uso fluente nei builder.
     */
    public Mechanism withReward(Attribute attribute, int amount) {
        this.reward = new AttributeReward(attribute, amount);
        return this;
    }

    public Optional<AttributeReward> getReward() {
        return Optional.ofNullable(reward);
    }

    /**
     * Imposta un indizio mostrato solo se il requisito è soddisfatto (es. abbastanza Sapienza).
     * L'indizio è una scorciatoia: l'enigma resta risolvibile anche senza.
     */
    public Mechanism withHint(Condition requirement, String text) {
        this.hint = new ConditionalText(requirement, text);
        return this;
    }

    public Optional<ConditionalText> getHint() {
        return Optional.ofNullable(hint);
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

    /**
     * Usato dal caricamento di un salvataggio: apre senza controllare le condizioni né dare bonus.
     */
    public void restoreUnlocked() {
        this.locked = false;
    }

    public Optional<Puzzle> getRequiredPuzzle() {
        return unlockCondition.requiredPuzzle();
    }
}