package it.unicam.cs.mpgc.rpg130579.view;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;

import java.util.List;

public record LocationView(String id, String name, String description, List<ItemView> items, List<NpcView> npcs, List<ExitView> exits, List<MechanismView> mechanisms) {
    public LocationView {
        items = List.copyOf(items);
        npcs = List.copyOf(npcs);
        exits = List.copyOf(exits);
    }
}
