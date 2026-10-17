package it.unicam.cs.mpgc.rpg130579.Model.Entity;

import it.unicam.cs.mpgc.rpg130579.Model.World.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player extends Character {
    private static final int DEFAULT_MAX_CAPACITY = 20;
    private final List<Item> inventory;

    public Player(String id, String name, String description, Location currentLocation) {
        super(id, name, description, currentLocation);
        this.inventory = new ArrayList<>();
    }

    public boolean hasItem(String itemId) {
        if (itemId == null || itemId.isBlank()) throw new IllegalArgumentException("L'ID dell'oggetto non deve essere vuoto");
        return inventory.stream().anyMatch(item -> item.getId().equalsIgnoreCase(itemId));
    }

    public boolean addItem(Item item) {
        if (item == null) throw new IllegalArgumentException("L'oggetto non può essere null");
        if (hasItem(item.getId())) return false;
        return inventory.add(item);
    }

    public Item getItemAt(int index) {
        if (index < 0 || index >= inventory.size()) throw new IllegalArgumentException("Indice non valido");
        return inventory.get(index);
    }

    public boolean removeItem(Item item) {
        return this.inventory.remove(item);
    }

    public List<Item> getInventory() {
        return Collections.unmodifiableList(inventory);
    }
}

