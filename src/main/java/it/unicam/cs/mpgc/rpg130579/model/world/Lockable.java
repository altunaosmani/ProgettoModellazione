package it.unicam.cs.mpgc.rpg130579.model.world;

public interface Lockable {
    Lockable NEVER_LOCKED = () -> false;

    boolean isLocked();

    default boolean isUnlocked() {
        return !isLocked();
    }
}
