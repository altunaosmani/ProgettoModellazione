package it.unicam.cs.mpgc.rpg130579.model.entity;

/**
 * Contratto per gli oggetti capaci di sbloccare un meccanismo identificato dal suo ID.
 */
public interface MechanismKey {
    /**
     * Verifica se questa chiave è compatibile con il meccanismo indicato.
     *
     * @param mechanismId identificativo del meccanismo
     * @return {@code true} se può sbloccarlo
     */
    boolean opens(String mechanismId);
}