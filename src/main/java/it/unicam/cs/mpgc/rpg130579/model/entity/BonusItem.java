package it.unicam.cs.mpgc.rpg130579.model.entity;

/**
 * Oggetto che conferisce un bonus permanente a un {@link Attribute} finché rimane
 * nell'inventario del giocatore.
 */
public class BonusItem extends Item implements AttributeModifier {
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
    public int bonusFor(Attribute requested) {
        return requested == attribute ? bonus : 0;
    }

    @Override
    public String examine() {
        return getDescription() + "\nBonus: +" + bonus + " " + attribute.getLabel() + " finché lo porti con te.";
    }
}