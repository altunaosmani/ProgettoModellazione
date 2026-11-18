package it.unicam.cs.mpgc.rpg130579.view;

/**
 * Stato sintetico della partita (completamento livello corrente, fine partita, vittoria).
 */
public record GameStatus(boolean levelCompleted, boolean gameOver, boolean won) {
}
