package it.unicam.cs.mpgc.rpg130579.Model.World;

import it.unicam.cs.mpgc.rpg130579.Model.Entity.Item;
import it.unicam.cs.mpgc.rpg130579.Model.Entity.NPC;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private final String id;
    private final String name;
    private final String description;
    private final List<Item> items;
    private final List<NPC> npcs;

    public Location(String id, String name, String description) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID del luogo non può essere vuoto");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Il nome del luogo non può essere vuoto");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("La descrizione del luogo non può essere vuota");
        this.id = id;
        this.name = name;
        this.description = description;
        this.items = new ArrayList<>();
        this.npcs = new ArrayList<>();
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getSizeNPC() {
        return this.npcs.size();
    }

    public int getSizeItems() {
        return this.items.size();
    }

    public NPC getNPCById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID del personaggio non può essere vuoto");
        for (NPC npc : npcs) {
            if (npc.getId().equals(id)) return npc;
        }
        return null;
    }

    public boolean addNPC(NPC x) {
        if (x == null) throw new IllegalArgumentException("Il personaggio non può essere null");
        if (getNPCById(x.getId()) != null) return false;
        npcs.add(x);
        return true;
    }

    public NPC getNPCAt(int index) {
        if (index < 0 || index >= npcs.size()) throw new IllegalArgumentException("L'indice del personaggio non è valido");
        return npcs.get(index);
    }

    public Item getItemsById(String id) {
        if (id == null) throw new IllegalArgumentException("L'oggetto non può essere nullo");
        for (Item item : items) {
            if (item.getId().equals(id)) return item;
        }
        return null;
    }

    public boolean addItem(Item i) {
        if (i == null) throw new IllegalArgumentException("L'oggetto non deve essere null");
        if (getItemsById(i.getId()) != null) return false;
        items.add(i);
        return true;
    }

    public boolean rimuoviOggetto(String id) {
        Item i = getItemsById(id);
        if (i == null) return false;
        items.remove(i);
        return true;
    }
}
