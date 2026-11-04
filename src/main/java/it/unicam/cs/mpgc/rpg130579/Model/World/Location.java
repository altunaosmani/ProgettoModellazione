package it.unicam.cs.mpgc.rpg130579.Model.World;

import it.unicam.cs.mpgc.rpg130579.Model.Entity.Item;
import it.unicam.cs.mpgc.rpg130579.Model.Entity.NPC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class Location {
    private final String id;
    private final String name;
    private final String description;
    private final List<Item> items;
    private final List<NPC> npcs;
    private final List<Exit> exits;

    public Location(String id, String name, String description) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID del luogo non può essere vuoto");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Il nome del luogo non può essere vuoto");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("La descrizione del luogo non può essere vuota");
        this.id = id;
        this.name = name;
        this.description = description;
        this.items = new ArrayList<>();
        this.npcs = new ArrayList<>();
        this.exits = new ArrayList<>();
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

    public boolean addNPC(NPC npc) {
        if (npc == null) throw new IllegalArgumentException("Il personaggio non può essere null");
        if (getNPCById(npc.getId()) != null) return false;
        npcs.add(npc);
        return true;
    }

    public NPC getNPCAt(int index) {
        if (index < 0 || index >= npcs.size()) throw new IllegalArgumentException("L'indice del personaggio non è valido");
        return npcs.get(index);
    }

    public Item getItemsById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'oggetto non può essere nullo");
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

    public boolean removeItem(String id) {
        Item i = getItemsById(id);
        if (i == null) return false;
        items.remove(i);
        return true;
    }

    public Exit getExitById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID dell'uscita on può essere vuoto");
        for (Exit exit : exits) {
            if (exit.getId().equalsIgnoreCase(id)) return exit;
        }
        return null;
    }

    public boolean addExit(Exit exit) {
        if (exit == null) throw new IllegalArgumentException("L'uscita non può essere nulla");
        if (getExitById(exit.getId()) != null) return false;
        exits.add(exit);
        return true;
    }

    public List<Exit> getExits() {
        return Collections.unmodifiableList(exits);
    }
}
