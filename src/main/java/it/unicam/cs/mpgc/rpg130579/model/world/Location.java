package it.unicam.cs.mpgc.rpg130579.model.world;

import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.NPC;
import it.unicam.cs.mpgc.rpg130579.model.interaction.ConditionalText;

import java.util.*;

/**
 * Rappresenta un singolo ambiente esplorabile del livello.
 * Indicizza oggetti, personaggi, uscite e meccanismi per ID (case-insensitive)
 * preservando l'ordine di inserimento, e può contenere dettagli ambientali condizionali.
 */
public class Location {
    private final String id;
    private final String name;
    private final String description;
    private final Map<String, Item> items = new LinkedHashMap<>();
    private final Map<String, NPC> npcs = new LinkedHashMap<>();
    private final Map<String, Exit> exits = new LinkedHashMap<>();
    private final Map<String, Mechanism> mechanisms = new LinkedHashMap<>();
    private final List<ConditionalText> details = new ArrayList<>();

    public Location(String id, String name, String description) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID del luogo non può essere vuoto");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Il nome del luogo non può essere vuoto");
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("La descrizione del luogo non può essere vuota");
        this.id = id;
        this.name = name;
        this.description = description;
    }

    private static String key(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " non può essere vuoto");
        return value.trim().toLowerCase(Locale.ROOT);
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

    // ---------- Dettagli condizionati ----------

    public void addDetail(ConditionalText detail) {
        if (detail == null) throw new IllegalArgumentException("Il dettaglio non può essere nullo");
        details.add(detail);
    }

    public List<ConditionalText> getDetails() {
        return List.copyOf(details);
    }

    // ---------- Personaggi ----------

    public NPC getNPCById(String npcId) {
        return npcs.get(key(npcId, "L'ID del personaggio"));
    }

    public boolean addNPC(NPC npc) {
        if (npc == null) throw new IllegalArgumentException("Il personaggio non può essere nullo");
        return npcs.putIfAbsent(key(npc.getId(), "L'ID del personaggio"), npc) == null;
    }

    public List<NPC> getNpcs() {
        return List.copyOf(npcs.values());
    }

    // ---------- Oggetti ----------

    public Item getItemById(String itemId) {
        return items.get(key(itemId, "L'ID dell'oggetto"));
    }

    public boolean addItem(Item item) {
        if (item == null) throw new IllegalArgumentException("L'oggetto non può essere nullo");
        return items.putIfAbsent(key(item.getId(), "L'ID dell'oggetto"), item) == null;
    }

    public boolean removeItem(String itemId) {
        return items.remove(key(itemId, "L'ID dell'oggetto")) != null;
    }

    public List<Item> getItems() {
        return List.copyOf(items.values());
    }

    // ---------- Uscite ----------

    public Exit getExitById(String exitId) {
        return exits.get(key(exitId, "L'ID dell'uscita"));
    }

    public boolean addExit(Exit exit) {
        if (exit == null) throw new IllegalArgumentException("L'uscita non può essere nulla");
        return exits.putIfAbsent(key(exit.getId(), "L'ID dell'uscita"), exit) == null;
    }

    public List<Exit> getExits() {
        return List.copyOf(exits.values());
    }

    // ---------- Meccanismi ----------

    public Mechanism getMechanismById(String mechanismId) {
        return mechanisms.get(key(mechanismId, "L'ID del meccanismo"));
    }

    public List<Mechanism> getMechanisms() {
        return List.copyOf(mechanisms.values());
    }

    /**
     * Solo per {@link Level#addMechanism(String, Mechanism)}, unico punto di registrazione.
     */
    boolean addMechanism(Mechanism mechanism) {
        if (mechanism == null) throw new IllegalArgumentException("Il meccanismo non può essere nullo");
        return mechanisms.putIfAbsent(key(mechanism.getId(), "L'ID del meccanismo"), mechanism) == null;
    }
}