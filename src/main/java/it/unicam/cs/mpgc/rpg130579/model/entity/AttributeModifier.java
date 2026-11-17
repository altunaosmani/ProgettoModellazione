package it.unicam.cs.mpgc.rpg130579.model.entity;

/**
 * Qualunque oggetto che modifica un attributo del giocatore finché è nell'inventario.
 * Player interroga questa interfaccia e non conosce le classi concrete (OCP):
 * un nuovo tipo di oggetto con bonus basta che la implementi.
 */
public interface AttributeModifier {
    /** Bonus fornito per l'attributo indicato; 0 se non lo riguarda. */
    int bonusFor(Attribute attribute);
}
