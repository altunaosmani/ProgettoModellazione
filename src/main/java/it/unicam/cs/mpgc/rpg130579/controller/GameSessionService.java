package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.model.interaction.DialogueSession;
import it.unicam.cs.mpgc.rpg130579.model.state.GameState;

/**
 * Contratto minimo delle operazioni di sessione richieste dal flusso dei dialoghi.
 */
public interface GameSessionService {
    GameState getCurrentState();

    DialogueSession interactWithNPC(String npcId);

    boolean completeCurrentQuest();
}