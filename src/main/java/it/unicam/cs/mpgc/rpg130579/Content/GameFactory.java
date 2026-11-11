package it.unicam.cs.mpgc.rpg130579.Content;

import it.unicam.cs.mpgc.rpg130579.Controller.GameState;
import it.unicam.cs.mpgc.rpg130579.Model.Entity.GenericItem;
import it.unicam.cs.mpgc.rpg130579.Model.Entity.Item;
import it.unicam.cs.mpgc.rpg130579.Model.Entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.Model.Entity.Player;
import it.unicam.cs.mpgc.rpg130579.Model.World.Level;
import it.unicam.cs.mpgc.rpg130579.Model.World.LevelManager;

import java.util.List;

public class GameFactory {
    private final List<LevelBuilder> levelBuilders;

    public GameFactory() {
        this(List.of(new ForestLevelBuilder()));
    }

    public GameFactory(List<LevelBuilder> levelBuilders) {
        if (levelBuilders == null || levelBuilders.isEmpty())
            throw new IllegalArgumentException("Serve almeno un livello");
        this.levelBuilders = List.copyOf(levelBuilders);
    }

    public GameSetup newGame() {
        ItemCatalog catalog = new ItemCatalog();
        Item medallion = new GenericItem(StoryIds.MEDALLION, "Medaglione d'argento", "Un piccolo medaglione d'argento con un occhio circondato da tre rami.");
        catalog.register(medallion);
        LevelManager levels = new LevelManager();
        for (LevelBuilder builder : levelBuilders) {
            Level level = builder.build(catalog);
            levels.addLevel(level);
        }

        Level first = levels.getLevelAt(0);
        Player player = new Player("eren", "Eren",
                "Giovane elfo del villaggio di Tanusha.", first.getStartLocation());
        player.addItem(medallion);

        return new GameSetup(new GameState(player, levels), catalog);
    }
}