package it.unicam.cs.mpgc.rpg130579.model.entity;

import java.util.Objects;

/**
 * Classe base astratta per tutti gli oggetti collezionabili nel mondo di gioco.
 * L'uguaglianza tra oggetti è basata esclusivamente sul loro {@code id}.
 */
public abstract class Item {
    private final String id;
    private final String name;
    private final String description;

    public Item(String id, String name, String description) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID dell'oggetto non può essere vuoto");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Il nome dell'oggetto non può essere vuoto");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("La descrizione dell'oggetto non può essere vuota");
        this.id = id;
        this.name = name;
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Restituisce il testo mostrato quando il giocatore esamina l'oggetto.
     *
     * @return descrizione dettagliata o contenuto leggibile dell'oggetto
     */
    public String examine() {
        return this.description;
    }
}
