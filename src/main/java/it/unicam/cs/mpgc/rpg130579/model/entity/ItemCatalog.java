package it.unicam.cs.mpgc.rpg130579.model.entity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class ItemCatalog {
    private final Map<String, Item> items = new HashMap<>();

    public void register(Item item) {
        if (item == null) throw new IllegalArgumentException("L'oggetto non può essere nullo");
        if (items.putIfAbsent(key(item.getId()), item) != null) throw new IllegalArgumentException("Esiste già un oggetto con questo id: " + item.getId());
    }

    public Optional<Item> findById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID dell'oggetto non può essere vuoto");
        return Optional.ofNullable((items.get(key(id))));
    }

    public int size() {
        return items.size();
    }

    private static String key(String id) {
        return id.trim().toLowerCase(Locale.ROOT);
    }
}
