package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Implementazione standard di {@link StatsTracker}: rileva gli incrementi negli attributi effettivi
 * del giocatore prima e dopo un'azione, formattando una nota testuale per il feedback all'utente.
 */
public class StatsChangeReporter implements StatsTracker {

    /**
     * Acquisisce un'istantanea immodificabile dei valori effettivi attuali degli attributi.
     *
     * @param player giocatore da ispezionare
     * @return mappa attributo -> valore effettivo corrente
     */
    @Override
    public Map<Attribute, Integer> capture(Player player) {
        if (player == null) throw new IllegalArgumentException("Il giocatore non può essere nullo");
        Map<Attribute, Integer> snapshot = new EnumMap<>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            snapshot.put(attribute, player.getAttributeValue(attribute));
        }
        return Collections.unmodifiableMap(snapshot);
    }

    /**
     * Confronta i valori attuali con una fotografia precedente e descrive gli eventuali aumenti.
     *
     * @param player giocatore aggiornato
     * @param before mappa acquisita prima dell'azione tramite {@link #capture(Player)}
     * @return stringa formattata (es. {@code " +1 Persuasione."}) oppure stringa vuota
     */
    @Override
    public String describeGains(Player player, Map<Attribute, Integer> before) {
        if (player == null) throw new IllegalArgumentException("Il giocatore non può essere nullo");
        if (before == null) throw new IllegalArgumentException("La fotografia precedente non può essere nulla");
        StringBuilder note = new StringBuilder();
        for (Attribute attribute : Attribute.values()) {
            int gain = player.getAttributeValue(attribute) - before.getOrDefault(attribute, 0);
            if (gain > 0) note.append(" +").append(gain).append(' ').append(attribute.getLabel()).append('.');
        }
        return note.toString();
    }
}
