package it.unicam.cs.mpgc.rpg130579.model.entity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Registro centrale di tutti gli {@link Item} definiti nel gioco, indicizzati per ID
 * (case-insensitive). Utilizzato durante il caricamento dei salvataggi per ricostruire l'inventario.
 */
public class ItemCatalog {
    private final Map<String, Item> items = new HashMap<>();

    private static String key(String id) {
        return id.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Registra un nuovo oggetto nel catalogo globale.
     *
     * @param item oggetto da registrare
     * @throws IllegalArgumentException se {@code item} è {@code null} o esiste già un oggetto con lo stesso ID
     */
    public void register(Item item) {
        if (item == null) throw new IllegalArgumentException("L'oggetto non può essere nullo");
        if (items.putIfAbsent(key(item.getId()), item) != null)
            throw new IllegalArgumentException("Esiste già un oggetto con questo id: " + item.getId());
    }

    /**
     * Cerca un oggetto registrato tramite il suo identificativo (case-insensitive).
     *
     * @param id identificativo dell'oggetto
     * @return {@link Optional} contenente l'oggetto se trovato, altrimenti vuoto
     * @throws IllegalArgumentException se {@code id} è {@code null} o vuoto
     */
    public Optional<Item> findById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID dell'oggetto non può essere vuoto");
        return Optional.ofNullable(items.get(key(id)));
    }

    /**
     * @return numero totale di oggetti registrati nel catalogo
     */
    public int size() {
        return items.size();
    }
}
