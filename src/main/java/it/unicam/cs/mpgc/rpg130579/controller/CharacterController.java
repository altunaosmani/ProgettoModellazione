package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.view.StatsView;

/**
 * Accesso alle statistiche e agli attributi del protagonista.
 */
public interface CharacterController {
    /**
     * @return vista aggiornata dei valori effettivi e dei bonus degli attributi del giocatore
     */
    StatsView stats();
}