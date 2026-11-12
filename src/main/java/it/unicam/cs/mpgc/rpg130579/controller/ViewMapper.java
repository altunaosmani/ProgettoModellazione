package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.NPC;
import it.unicam.cs.mpgc.rpg130579.model.world.Exit;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import it.unicam.cs.mpgc.rpg130579.view.ExitView;
import it.unicam.cs.mpgc.rpg130579.view.ItemView;
import it.unicam.cs.mpgc.rpg130579.view.LocationView;
import it.unicam.cs.mpgc.rpg130579.view.NpcView;

import java.util.List;

final class ViewMapper {

    ItemView toView(Item item) {
        return new ItemView(item.getId(), item.getName(), item.getDescription(), item.examine());
    }

    ExitView toView(Exit exit) {
        return new ExitView(exit.getId(), exit.getLabel(), exit.isPassable());
    }

    NpcView toView(NPC npc) {
        return new NpcView(npc.getId(), npc.getName(), npc.getDescription());
    }

    LocationView toView(Location location) {
        return new LocationView(
                location.getId(),
                location.getName(),
                location.getDescription(),
                location.getItems().stream().map(this::toView).toList(),
                location.getNpcs().stream().map(this::toView).toList(),
                location.getExits().stream().map(this::toView).toList());
    }

    List<ItemView> toItemViews(List<Item> items) {
        return items.stream().map(this::toView).toList();
    }
}
