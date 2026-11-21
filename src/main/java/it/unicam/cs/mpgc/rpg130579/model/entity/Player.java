package it.unicam.cs.mpgc.rpg130579.model.entity;

import it.unicam.cs.mpgc.rpg130579.model.world.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Rappresenta il protagonista controllato dal giocatore.
 * <p>
 * Mantiene l'inventario (con capacità massima predefinita di 20 oggetti senza duplicati)
 * e le statistiche base, calcolando dinamicamente i valori effettivi degli attributi
 * sommando i bonus forniti dagli oggetti {@link AttributeModifier} trasportati.
 */
public class Player extends GameCharacter {
    public static final int MAX_CAPACITY = 20;
    private final List<Item> inventory;
    private final CharacterStats stats = new CharacterStats();

    /**
     * Crea un nuovo protagonista posizionato nel luogo iniziale indicato.
     *
     * @param id              identificativo univoco del giocatore
     * @param name            nome del protagonista
     * @param description     descrizione del personaggio
     * @param currentLocation luogo di partenza
     */
    public Player(String id, String name, String description, Location currentLocation) {
        super(id, name, description, currentLocation);
        this.inventory = new ArrayList<>();
    }

    /**
     * @return gestore delle statistiche base del protagonista
     */
    public CharacterStats getStats() {
        return stats;
    }

    /**
     * Calcola la somma dei bonus forniti dagli oggetti nell'inventario per un dato attributo.
     *
     * @param attribute attributo di interesse
     * @return bonus totale derivante dall'inventario
     */
    public int getAttributeBonus(Attribute attribute) {
        if (attribute == null) throw new IllegalArgumentException("L'attributo non può essere nullo");
        return inventory.stream()
                .filter(AttributeModifier.class::isInstance)
                .map(AttributeModifier.class::cast)
                .mapToInt(modifier -> modifier.bonusFor(attribute))
                .sum();
    }

    /**
     * Restituisce il valore effettivo dell'attributo (valore base + bonus degli oggetti).
     *
     * @param attribute attributo di interesse
     * @return valore effettivo utilizzato per verificare condizioni e finali
     */
    public int getAttributeValue(Attribute attribute) {
        return stats.get(attribute) + getAttributeBonus(attribute);
    }

    /**
     * Verifica se nell'inventario è presente un oggetto con l'ID specificato (case-insensitive).
     *
     * @param itemId identificativo dell'oggetto da cercare
     * @return {@code true} se l'oggetto è nell'inventario
     * @throws IllegalArgumentException se {@code itemId} è {@code null} o vuoto
     */
    public boolean hasItem(String itemId) {
        if (itemId == null || itemId.isBlank())
            throw new IllegalArgumentException("L'ID dell'oggetto non deve essere vuoto");
        return inventory.stream().anyMatch(item -> item.getId().equalsIgnoreCase(itemId));
    }

    /**
     * @return {@code true} se l'inventario ha raggiunto la capienza massima ({@value #MAX_CAPACITY})
     */
    public boolean isInventoryFull() {
        return inventory.size() >= MAX_CAPACITY;
    }

    /**
     * Aggiunge un oggetto all'inventario se non è già presente e se non è stata raggiunta la capienza massima.
     *
     * @param item oggetto da aggiungere
     * @return {@code true} se l'inserimento ha avuto successo, {@code false} altrimenti
     */
    public boolean addItem(Item item) {
        if (item == null) throw new IllegalArgumentException("L'oggetto non può essere null");
        if (isInventoryFull()) return false;
        if (hasItem(item.getId())) return false;
        return inventory.add(item);
    }

    /**
     * Rimuove l'oggetto specificato dall'inventario.
     *
     * @param item oggetto da rimuovere
     * @return {@code true} se l'oggetto era presente ed è stato rimosso
     */
    public boolean removeItem(Item item) {
        return this.inventory.remove(item);
    }

    /**
     * @return vista non modificabile degli oggetti attualmente nell'inventario
     */
    public List<Item> getInventory() {
        return Collections.unmodifiableList(inventory);
    }
}
