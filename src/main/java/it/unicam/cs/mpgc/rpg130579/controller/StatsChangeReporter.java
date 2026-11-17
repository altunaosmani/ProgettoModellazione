package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/** Calcola quali attributi del giocatore sono aumentati dopo un'azione e ne produce il testo per il messaggio. */
public class StatsChangeReporter {

    /** Fotografia dei valori effettivi (base + bonus oggetti), da confrontare dopo l'azione. */
    public Map<Attribute, Integer> capture(Player player) {
        if (player == null) throw new IllegalArgumentException("Il giocatore non può essere nullo");
        Map<Attribute, Integer> snapshot = new EnumMap<>(Attribute.class);
        for (Attribute attribute : Attribute.values()) {
            snapshot.put(attribute, player.getAttributeValue(attribute));
        }
        return Collections.unmodifiableMap(snapshot);
    }

    /** Es. " +1 Persuasione. +1 Sapienza." oppure stringa vuota se nulla è aumentato. */
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