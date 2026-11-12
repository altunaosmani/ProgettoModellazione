package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.NPC;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.interaction.Puzzle;
import it.unicam.cs.mpgc.rpg130579.model.state.EndingScreen;
import it.unicam.cs.mpgc.rpg130579.model.world.Exit;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import it.unicam.cs.mpgc.rpg130579.model.world.Mechanism;
import it.unicam.cs.mpgc.rpg130579.view.*;

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

    PuzzleView toView(Puzzle puzzle, Player player) {
        return new PuzzleView(puzzle.getDescription(), puzzle.choices(player), puzzle.answerSize());
    }

    MechanismView toView(Mechanism mechanism, Player player) {
        PuzzleView puzzle = mechanism.getRequiredPuzzle()
                .map(p -> toView(p, player))
                .orElse(null);
        return new MechanismView(mechanism.getId(), mechanism.getName(),
                mechanism.getDescription(), mechanism.isLocked(), puzzle);
    }

    LocationView toView(Location location, Player player) {
        return new LocationView(
                location.getId(),
                location.getName(),
                location.getDescription(),
                location.getItems().stream().map(this::toView).toList(),
                location.getNpcs().stream().map(this::toView).toList(),
                location.getExits().stream().map(this::toView).toList(),
                location.getMechanisms().stream().map(m -> toView(m, player)).toList());
    }

    List<ItemView> toItemViews(List<Item> items) {
        return items.stream().map(this::toView).toList();
    }

    EndingView toView(EndingScreen e) {
        return new EndingView(e.gameTitle(), e.headline(), e.subtitle(), e.message(), e.outcome());
    }
}
