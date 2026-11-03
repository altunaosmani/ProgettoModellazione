package it.unicam.cs.mpgc.rpg130579.Model.World;

import it.unicam.cs.mpgc.rpg130579.Model.Entity.Player;
import it.unicam.cs.mpgc.rpg130579.Model.Interaction.PuzzleAnswer;

public record UnlockAttempt (String mechanismId, Player player, PuzzleAnswer answer) {
    public UnlockAttempt {
        if (mechanismId == null || mechanismId.isBlank()) throw new IllegalArgumentException("L'ID del meccanismo non può essere vuoto");
        if (player == null) throw new IllegalArgumentException("Il giocatore non può essere nullo");
        if (answer == null) answer = PuzzleAnswer.empty();
    }
}
