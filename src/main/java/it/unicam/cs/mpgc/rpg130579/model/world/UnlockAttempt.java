package it.unicam.cs.mpgc.rpg130579.model.world;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.interaction.PuzzleAnswer;

/**
 * Dati di un tentativo di sblocco: ID del meccanismo, giocatore e risposta fornita.
 */
public record UnlockAttempt(String mechanismId, Player player, PuzzleAnswer answer) {
    public UnlockAttempt {
        if (mechanismId == null || mechanismId.isBlank())
            throw new IllegalArgumentException("L'ID del meccanismo non può essere vuoto");
        if (player == null) throw new IllegalArgumentException("Il giocatore non può essere nullo");
        if (answer == null) answer = PuzzleAnswer.empty();
    }
}
