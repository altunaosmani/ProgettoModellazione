package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.world.Level;

/**
 * Contratto per la costruzione di un singolo {@link Level} del gioco.
 * <p>
 * Ogni implementazione definisce luoghi, collegamenti, enigmi, meccanismi,
 * personaggi non giocanti e oggetti, registrando questi ultimi nel catalogo condiviso.
 */
public interface LevelBuilder {
    /**
     * Costruisce e configura l'intero livello.
     *
     * @param catalog catalogo globale in cui registrare tutti gli oggetti creati nel livello
     * @return il livello completamente configurato e pronto all'uso
     */
    Level build(ItemCatalog catalog);
}
