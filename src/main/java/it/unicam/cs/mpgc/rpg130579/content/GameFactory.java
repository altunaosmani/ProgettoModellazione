package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.entity.DocumentItem;
import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.GameState;
import it.unicam.cs.mpgc.rpg130579.model.world.Level;
import it.unicam.cs.mpgc.rpg130579.model.world.LevelManager;

import java.util.List;

/**
 * Factory responsabile della creazione e configurazione iniziale di una nuova partita.
 * <p>
 * Popola il catalogo globale degli oggetti, costruisce la sequenza dei livelli
 * tramite i rispettivi {@link LevelBuilder}, inizializza il protagonista con
 * l'equipaggiamento di partenza e assembla il {@link GameSetup} completo.
 */
public class GameFactory {
    private final List<LevelBuilder> levelBuilders;

    /**
     * Crea una factory preconfigurata con i tre livelli standard della storia
     * (Foresta, Castello, Lago).
     */
    public GameFactory() {
        this(List.of(new ForestLevelBuilder(), new CastleLevelBuilder(), new LakeLevelBuilder()));
    }

    /**
     * Crea una factory con una lista personalizzata di builder di livelli.
     *
     * @param levelBuilders lista ordinata dei builder da utilizzare
     * @throws IllegalArgumentException se la lista è {@code null} o vuota
     */
    public GameFactory(List<LevelBuilder> levelBuilders) {
        if (levelBuilders == null || levelBuilders.isEmpty())
            throw new IllegalArgumentException("Serve almeno un livello");
        this.levelBuilders = List.copyOf(levelBuilders);
    }

    /**
     * Genera una nuova istanza completa e indipendente di {@link GameSetup}.
     *
     * @return configurazione iniziale pronta per avviare una partita
     */
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
        return new GameSetup(new GameState(player, levels), catalog, StoryIntros.prologue(), StoryEndings.endings());
    }
}