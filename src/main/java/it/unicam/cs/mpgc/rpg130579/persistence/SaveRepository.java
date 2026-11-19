package it.unicam.cs.mpgc.rpg130579.persistence;

import java.util.Optional;

/**
 * Astrazione dello storage fisico per la lettura e scrittura di {@link SaveData}.
 */
public interface SaveRepository {
    /**
     * Persiste lo snapshot dei dati di salvataggio nello storage sottostante.
     *
     * @param data dati della partita da salvare
     */
    void save(SaveData data);

    /**
     * Carica i dati di salvataggio dallo storage, se presenti.
     *
     * @return {@link Optional} contenente i dati caricati, oppure vuoto se non esiste alcun salvataggio
     */
    Optional<SaveData> load();
}
