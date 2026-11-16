package it.unicam.cs.mpgc.rpg130579.view;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * values: valore effettivo di ogni attributo (nome da mostrare -> valore).
 * bonuses: la parte del valore che viene dagli oggetti nell'inventario.
 */
public record StatsView(Map<String, Integer> values, Map<String, Integer> bonuses) {
    public StatsView {
        values = Collections.unmodifiableMap(new LinkedHashMap<>(values));
        bonuses = Collections.unmodifiableMap(new LinkedHashMap<>(bonuses));
    }
}