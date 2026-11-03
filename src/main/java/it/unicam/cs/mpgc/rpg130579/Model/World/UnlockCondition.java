package it.unicam.cs.mpgc.rpg130579.Model.World;

public interface UnlockCondition {
    boolean isSatisfiedBy(UnlockAttempt attempt);
}
