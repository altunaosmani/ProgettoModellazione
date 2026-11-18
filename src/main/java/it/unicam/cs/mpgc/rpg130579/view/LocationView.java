package it.unicam.cs.mpgc.rpg130579.view;

import java.util.List;

/** Dati di presentazione di un luogo esplorabile e dei suoi elementi visibili. */
public record LocationView(String id, String name, String description, List<ItemView> items, List<NpcView> npcs,
                           List<ExitView> exits, List<MechanismView> mechanisms, List<String> details) {
    public LocationView {
        items = List.copyOf(items);
        npcs = List.copyOf(npcs);
        exits = List.copyOf(exits);
        mechanisms = List.copyOf(mechanisms);
        details = List.copyOf(details);
    }
}