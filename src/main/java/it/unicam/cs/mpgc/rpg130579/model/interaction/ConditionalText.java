package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;

/**
 * Testo narrativo (dettaglio di un luogo o indizio di un meccanismo) visibile solo
 * quando la {@link Condition} associata è soddisfatta.
 */
public record ConditionalText(Condition requirement, String text) {
    public ConditionalText {
        if (text == null || text.isBlank()) throw new IllegalArgumentException("Il testo non può essere vuoto");
        if (requirement == null) requirement = Condition.ALWAYS;
    }

    public boolean isVisibleTo(Player player, StoryProgress progress) {
        return requirement.isMetBy(player, progress);
    }
}