package it.unicam.cs.mpgc.rpg130579.model.entity;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Gestisce i valori base degli attributi di un personaggio.
 * Gli attributi non ancora incrementati hanno valore predefinito pari a {@code 0}.
 */
public class CharacterStats {
    private final EnumMap<Attribute, Integer> values = new EnumMap<>(Attribute.class);

    /**
     * Restituisce il valore base dell'attributo richiesto.
     *
     * @param attribute attributo da leggere
     * @return valore base corrente (0 se mai incrementato)
     */
    public int get(Attribute attribute) {
        if (attribute == null) throw new IllegalArgumentException("L'attributo non può essere nullo");
        return values.getOrDefault(attribute, 0);
    }

    /**
     * Incrementa il valore base di un attributo di una quantità positiva.
     *
     * @param attribute attributo da aumentare
     * @param amount    quantità da sommare (deve essere {@code > 0})
     * @throws IllegalArgumentException se l'attributo è {@code null} o l'incremento non è positivo
     */
    public void increase(Attribute attribute, int amount) {
        if (attribute == null) throw new IllegalArgumentException("L'attributo non può essere nullo");
        if (amount <= 0) throw new IllegalArgumentException("L'incremento deve essere positivo");
        values.merge(attribute, amount, Integer::sum);
    }

    /**
     * Copia non modificabile dei soli attributi con un valore assegnato.
     * */
    public Map<Attribute, Integer> asMap() {
        return Collections.unmodifiableMap(new EnumMap<>(values));
    }

    /**
     * Sovrascrive completamente i valori base a partire da una mappa salvata.
     *
     * @param saved mappa dei valori da ripristinare
     * @throws IllegalArgumentException se la mappa contiene chiavi nulle o valori negativi
     */
    public void restore(Map<Attribute, Integer> saved) {
        if (saved == null) throw new IllegalArgumentException("I valori salvati non possono essere nulli");
        for (Map.Entry<Attribute, Integer> e : saved.entrySet()) {
            if (e.getKey() == null || e.getValue() == null || e.getValue() < 0)
                throw new IllegalArgumentException("Valore di attributo non valido");
        }
        values.clear();
        values.putAll(saved);
    }
}
