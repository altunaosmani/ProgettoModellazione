package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.controller.GameState;
import it.unicam.cs.mpgc.rpg130579.model.entity.*;
import it.unicam.cs.mpgc.rpg130579.model.world.Level;
import it.unicam.cs.mpgc.rpg130579.model.world.LevelManager;

import java.util.List;

public class GameFactory {
    private final List<LevelBuilder> levelBuilders;

    public GameFactory() {
        this(List.of(new ForestLevelBuilder(), new CastleLevelBuilder(), new LakeLevelBuilder()));    }

    public GameFactory(List<LevelBuilder> levelBuilders) {
        if (levelBuilders == null || levelBuilders.isEmpty()) throw new IllegalArgumentException("Serve almeno un livello");
        this.levelBuilders = List.copyOf(levelBuilders);
    }

    public GameSetup newGame() {
        ItemCatalog catalog = new ItemCatalog();
        Item medallion = new DocumentItem(StoryIds.MEDALLION, "Medaglione d'argento",
                "Un piccolo medaglione d'argento con un occhio circondato da tre rami.",
                "Il medaglione si è aperto e rivela una frase: \"Quando la foresta parlerà, ascolta.\"");
        catalog.register(medallion);
        LevelManager levels = new LevelManager();
        for (LevelBuilder builder : levelBuilders) {
            Level level = builder.build(catalog);
            levels.addLevel(level);
        }
        Level first = levels.getLevelAt(0);
        Player player = new Player("eren", "Eren", "Giovane elfo del villaggio di Tanusha.", first.getStartLocation());
        player.addItem(medallion);
        return new GameSetup(new GameState(player, levels), catalog, StoryEndings.victory());    }
}