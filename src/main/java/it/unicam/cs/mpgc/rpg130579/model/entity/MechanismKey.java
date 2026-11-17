package it.unicam.cs.mpgc.rpg130579.model.entity;

/** Oggetto in grado di aprire un meccanismo. Usato da KeyUnlockCondition senza conoscere KeyItem. */
public interface MechanismKey {
    boolean opens(String mechanismId);
}