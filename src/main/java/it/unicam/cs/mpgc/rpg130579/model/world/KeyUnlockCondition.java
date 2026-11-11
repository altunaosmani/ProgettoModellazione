package it.unicam.cs.mpgc.rpg130579.model.world;

import it.unicam.cs.mpgc.rpg130579.model.entity.KeyItem;

public class KeyUnlockCondition implements UnlockCondition {
    @Override
    public boolean isSatisfiedBy(UnlockAttempt attempt) {
        return attempt.player().getInventory().stream().filter(KeyItem.class::isInstance).map(KeyItem.class::cast).anyMatch(key -> key.opens(attempt.mechanismId()));
    }
}
