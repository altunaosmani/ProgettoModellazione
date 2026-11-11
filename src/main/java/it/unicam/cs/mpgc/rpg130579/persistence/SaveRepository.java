package it.unicam.cs.mpgc.rpg130579.persistence;

import java.util.Optional;

public interface SaveRepository {
    void save(SaveData data);

    Optional<SaveData> load();
}
