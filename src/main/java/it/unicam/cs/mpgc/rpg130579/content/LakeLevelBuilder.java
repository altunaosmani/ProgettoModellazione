package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.content.LevelBuilder;
import it.unicam.cs.mpgc.rpg130579.model.entity.DocumentItem;
import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.entity.NPC;
import it.unicam.cs.mpgc.rpg130579.model.interaction.ChoicePuzzle;
import it.unicam.cs.mpgc.rpg130579.model.interaction.FlagCondition;
import it.unicam.cs.mpgc.rpg130579.model.interaction.Puzzle;
import it.unicam.cs.mpgc.rpg130579.model.interaction.Quest;
import it.unicam.cs.mpgc.rpg130579.model.world.Exit;
import it.unicam.cs.mpgc.rpg130579.model.world.Level;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import it.unicam.cs.mpgc.rpg130579.model.world.Mechanism;
import it.unicam.cs.mpgc.rpg130579.model.world.PuzzleUnlockCondition;

import java.util.LinkedHashMap;
import java.util.Map;

public class LakeLevelBuilder implements LevelBuilder {
    private static final String SHORE = "riva";
    private static final String LAKE_PATH = "sentiero_lago";
    private static final String STONE = "pietra_custodi";
    private static final String CLEARING = "radura";

    private static final String MOON_WATER_PUZZLE = "puzzle_luna_acqua";
    private static final String MOON_WATER_MECHANISM = "varco_luna_acqua";

    @Override
    public Level build(ItemCatalog catalog) {
        Level level = new Level("livello3", "Il Lago degli Specchi",
                "Un lago nascosto tra le montagne: qui finiscono le tracce di Elara.",
                new Quest("quest3", "Ritrova Elara",
                        "Segui gli ultimi segni dei Custodi e scopri la verità.",
                        new FlagCondition(LakeDialogues.ELARA_FOUND_FLAG, LakeDialogues.ELARA_FOUND_VALUE)));
        addLocations(level);
        addPuzzleAndMechanism(level);
        connectLocations(level);
        placeItems(level, catalog);
        placeNpcs(level);
        level.setStartLocation(SHORE);
        return level;
    }

    private void addLocations(Level level) {
        level.addPlace(new Location(SHORE, "Riva del Lago degli Specchi",
                "Un lago nascosto tra le montagne e circondato dalla foresta. L'acqua è così ferma da riflettere il cielo come uno specchio."));
        level.addPlace(new Location(LAKE_PATH, "Sentiero lungo il lago",
                "Un sentiero che costeggia l'acqua. Qualcuno è passato di qui, lasciando dei segni."));
        level.addPlace(new Location(STONE, "Pietra dei Custodi",
                "Una pietra levigata con il simbolo dei Custodi della Luna: un occhio circondato da tre rami. "
                        + "Sotto è incisa una frase: \"Dove la luna incontra l'acqua\"."));
        level.addPlace(new Location(CLEARING, "Radura sulla riva",
                "Una piccola radura sulle rive del lago. C'è qualcuno ad aspettarti."));
    }

    private void addPuzzleAndMechanism(Level level) {
        Map<String, String> options = new LinkedHashMap<>();
        options.put("monte", "La vetta delle montagne");
        options.put("centro", "Il centro del lago");
        options.put("riva", "La riva dove la luna si riflette");
        Puzzle moonWater = new ChoicePuzzle(MOON_WATER_PUZZLE,
                "\"Dove la luna incontra l'acqua\". Verso quale punto ti dirigi?", options, "riva");
        level.addPuzzle(moonWater);

        Mechanism gap = new Mechanism(MOON_WATER_MECHANISM, "Varco tra i canneti",
                "Un passaggio nascosto che si apre solo a chi comprende la frase dei Custodi.",
                new PuzzleUnlockCondition(moonWater));
        level.addMechanism(gap);
        level.getPlaceById(STONE).addMechanism(gap);
    }

    private void connectLocations(Level level) {
        Mechanism gap = level.getMechanismById(MOON_WATER_MECHANISM);

        level.getPlaceById(SHORE).addExit(new Exit(LAKE_PATH, "Segui il sentiero lungo il lago", LAKE_PATH));

        Location path = level.getPlaceById(LAKE_PATH);
        path.addExit(new Exit(SHORE, "Torna alla riva", SHORE));
        path.addExit(new Exit(STONE, "Avvicinati alla pietra", STONE));

        Location stone = level.getPlaceById(STONE);
        stone.addExit(new Exit(LAKE_PATH, "Torna al sentiero", LAKE_PATH));
        stone.addExit(new Exit(CLEARING, "Attraversa il varco tra i canneti", CLEARING, gap));

        level.getPlaceById(CLEARING).addExit(new Exit(STONE, "Torna alla pietra", STONE));
    }

    private void placeItems(Level level, ItemCatalog catalog) {
        place(level, catalog, LAKE_PATH, new DocumentItem("segno_elara", "Segno sul tronco",
                "Un segno inciso su un albero lungo il sentiero.",
                "Il simbolo dei Custodi della Luna, inciso con cura: sono gli ultimi segni lasciati da tua madre."));
        place(level, catalog, STONE, new DocumentItem("appunto_luna", "Appunto di Elara",
                "Un foglietto piegato ai piedi della pietra.",
                "\"Dove la luna incontra l'acqua, lì finisce la ricerca.\""));
    }

    private void placeNpcs(Level level) {
        Location clearing = level.getPlaceById(CLEARING);
        clearing.addNPC(new NPC("elara", "Elara", "Tua madre, finalmente ritrovata.",
                clearing, LakeDialogues.elara()));
    }

    private void place(Level level, ItemCatalog catalog, String locationId, Item item) {
        catalog.register(item);
        level.getPlaceById(locationId).addItem(item);
    }
}
