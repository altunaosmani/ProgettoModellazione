package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;

/**
 * Un testo che il giocatore vede solo se il requisito è soddisfatto
 * (es. un dettaglio notato solo con abbastanza Percezione, o un indizio che richiede Sapienza).
 */
public record ConditionalText(QuestCondition requirement, String text) {
    public ConditionalText {
        if (text == null || text.isBlank()) throw new IllegalArgumentException("Il testo non può essere vuoto");
        if (requirement == null) requirement = QuestCondition.ALWAYS;
    }

    public boolean isVisibleTo(Player player, StoryProgress progress) {
        return requirement.isMetBy(player, progress);
    }
}