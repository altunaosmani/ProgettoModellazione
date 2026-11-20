package it.unicam.cs.mpgc.rpg130579.model.state;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.interaction.Condition;

import java.util.List;

/**
 * Seleziona la schermata finale appropriata valutando in ordine una lista di varianti
 * condizionali e ripiegando su un finale predefinito se nessuna condizione è soddisfatta.
 */
public class EndingCatalog {
    private final EndingScreen fallback;
    private final List<Variant> variants;

    public EndingCatalog(EndingScreen fallback, List<Variant> variants) {
        if (fallback == null) throw new IllegalArgumentException("Il finale predefinito non può essere nullo");
        if (variants == null) throw new IllegalArgumentException("La lista delle varianti non può essere nulla");
        for (Variant variant : variants) {
            if (variant == null) throw new IllegalArgumentException("La variante non può essere nulla");
        }
        this.fallback = fallback;
        this.variants = List.copyOf(variants);
    }

    public EndingCatalog(EndingScreen fallback) {
        this(fallback, List.of());
    }

    public EndingScreen select(Player player, StoryProgress progress) {
        if (player == null) throw new IllegalArgumentException("Il giocatore non può essere nullo");
        if (progress == null) throw new IllegalArgumentException("Lo stato della storia non può essere nullo");
        return variants.stream()
                .filter(v -> v.condition().isMetBy(player, progress))
                .map(Variant::ending)
                .findFirst()
                .orElse(fallback);
    }

    /**
     * Un finale alternativo con la condizione che lo attiva.
     */
    public record Variant(Condition condition, EndingScreen ending) {
        public Variant {
            if (condition == null) throw new IllegalArgumentException("La condizione non può essere nulla");
            if (ending == null) throw new IllegalArgumentException("Il finale non può essere nullo");
        }
    }
}