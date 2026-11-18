package it.unicam.cs.mpgc.rpg130579.model.world;

import it.unicam.cs.mpgc.rpg130579.model.entity.KeyItem;
import it.unicam.cs.mpgc.rpg130579.model.entity.MechanismKey;

/** Condizione di sblocco soddisfatta se il giocatore possiede una {@link KeyItem} compatibile. */
public class KeyUnlockCondition implements UnlockCondition {
    @Override
    public boolean isSatisfiedBy(UnlockAttempt attempt) {
        return attempt.player().getInventory().stream()
                .filter(MechanismKey.class::isInstance)
                .map(MechanismKey.class::cast)
                .anyMatch(key -> key.opens(attempt.mechanismId()));
    }
}
