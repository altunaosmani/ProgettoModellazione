package it.unicam.cs.mpgc.rpg130579.persistence;

import java.util.Optional;

/**
 * Astrazione dello storage fisico per la lettura e scrittura di {@link SaveData}.
 */
public interface SaveRepository {
    void save(SaveData data);

    Optional<SaveData> load();
}
