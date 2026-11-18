package it.unicam.cs.mpgc.rpg130579.model.entity;

/**
 * Interfaccia implementata dagli elementi (come oggetti nell'inventario) che conferiscono
 * un bonus passivo agli attributi del giocatore.
 */
public interface AttributeModifier {
    /**
     * Restituisce il bonus fornito per l'attributo specificato.
     *
     * @param attribute attributo interrogato
     * @return valore del bonus (positivo se presente, {@code 0} altrimenti)
     */
    int bonusFor(Attribute attribute);
}
