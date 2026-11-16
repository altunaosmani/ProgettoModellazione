package it.unicam.cs.mpgc.rpg130579.view;

import java.util.List;

/** details: testi extra del luogo che il giocatore può vedere in questo momento (es. dettagli notati con la Percezione). */
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