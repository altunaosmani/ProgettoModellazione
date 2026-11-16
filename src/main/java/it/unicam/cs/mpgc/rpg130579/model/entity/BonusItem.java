package it.unicam.cs.mpgc.rpg130579.model.entity;

/**
 * Oggetto che aumenta un attributo finché si trova nell'inventario del giocatore.
 * Il bonus non viene sommato al valore base: lo calcola Player, così il salvataggio
 * resta invariato (basta l'inventario).
 */
public class BonusItem extends Item {
    private final Attribute attribute;
    private final int bonus;

    public BonusItem(String id, String name, String description, Attribute attribute, int bonus) {
        super(id, name, description);
        if (attribute == null) throw new IllegalArgumentException("L'attributo non può essere nullo");
        if (bonus <= 0) throw new IllegalArgumentException("Il bonus deve essere positivo");
        this.attribute = attribute;
        this.bonus = bonus;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public int getBonus() {
        return bonus;
    }

    @Override
    public String examine() {
        return getDescription() + "\nBonus: +" + bonus + " " + attribute.getLabel() + " finché lo porti con te.";
    }
}
