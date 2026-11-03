package it.unicam.cs.mpgc.rpg130579.Model.World;

public interface Lockable {
    Lockable NEVER_LOCKED = () -> false;

    boolean isLocked();

    default boolean isUnlocked() {
        return !isLocked();
    }
}
