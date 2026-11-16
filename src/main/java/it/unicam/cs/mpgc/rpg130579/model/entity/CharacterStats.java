package it.unicam.cs.mpgc.rpg130579.model.entity;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/** Valori degli attributi di un personaggio. Un attributo non ancora assegnato vale 0. */
public class CharacterStats {
    private final EnumMap<Attribute, Integer> values = new EnumMap<>(Attribute.class);

    public int get(Attribute attribute) {
        if (attribute == null) throw new IllegalArgumentException("L'attributo non può essere nullo");
        return values.getOrDefault(attribute, 0);
    }

    public void increase(Attribute attribute, int amount) {
        if (attribute == null) throw new IllegalArgumentException("L'attributo non può essere nullo");
        if (amount <= 0) throw new IllegalArgumentException("L'incremento deve essere positivo");
        values.merge(attribute, amount, Integer::sum);
    }

    /** Copia non modificabile dei soli attributi con un valore assegnato. */
    public Map<Attribute, Integer> asMap() {
        return Collections.unmodifiableMap(new EnumMap<>(values));
    }

    /** Usato dal caricamento di un salvataggio: sostituisce tutti i valori. */
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
