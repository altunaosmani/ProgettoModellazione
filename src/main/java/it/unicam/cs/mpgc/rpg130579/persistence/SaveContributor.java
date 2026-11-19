package it.unicam.cs.mpgc.rpg130579.persistence;

import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.state.GameState;

import java.util.Map;

/**
 * Estensione del salvataggio: una funzionalità aggiuntiva (es. stato degli NPC, missioni
 * secondarie) salva e ripristina la propria sezione senza modificare {@link SaveService}.
 */
public interface SaveContributor {
    /**
     * @return nome stabile e univoco della sezione nel file di salvataggio
     */
    String key();

    /**
     * @return i dati da salvare nella propria sezione
     */
    Map<String, String> capture(GameState state);

    /**
     * Valida i dati salvati senza modificare lo stato.
     *
     * @return l'azione che applica il ripristino, eseguita solo se tutto il salvataggio è valido
     * @throws IllegalStateException se i dati non sono coerenti col contenuto di gioco
     */
    Runnable prepareRestore(GameState state, ItemCatalog catalog, Map<String, String> saved);
}