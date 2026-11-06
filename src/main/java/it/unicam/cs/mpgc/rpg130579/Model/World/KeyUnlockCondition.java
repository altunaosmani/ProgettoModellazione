package it.unicam.cs.mpgc.rpg130579.Model.World;

import it.unicam.cs.mpgc.rpg130579.Model.Entity.KeyItem;

public class KeyUnlockCondition implements UnlockCondition {
    @Override
    public boolean isSatisfiedBy(UnlockAttempt attempt) {
        return attempt.player().getInventory().stream().filter(KeyItem.class::isInstance).map(KeyItem.class::cast).anyMatch(key -> key.opens(attempt.mechanismId()));
    }
}
