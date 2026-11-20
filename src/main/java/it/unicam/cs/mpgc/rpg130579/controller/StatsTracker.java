package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import java.util.Map;

/**
 * Contratto per il tracciamento e la descrizione delle variazioni agli attributi del giocatore.
 */
public interface StatsTracker {
    Map<Attribute, Integer> capture(Player player);

    String describeGains(Player player, Map<Attribute, Integer> before);
}