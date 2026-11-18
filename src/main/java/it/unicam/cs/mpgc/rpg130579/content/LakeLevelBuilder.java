package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.entity.DocumentItem;
import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.entity.NPC;
import it.unicam.cs.mpgc.rpg130579.model.interaction.AttributeCondition;
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

/**
 * Costruisce il terzo e ultimo livello, "Il Lago degli Specchi".
 * <p>
 * Il giocatore segue il sentiero fino alla Pietra dei Custodi e risolve l'enigma
 * "dove la luna incontra l'acqua" ({@link ChoicePuzzle}) per raggiungere la radura dove
 * si trova Elara. La missione si completa quando il dialogo con Elara imposta il flag
 * {@code elara_ritrovata}.
 */
public class LakeLevelBuilder implements LevelBuilder {
    private static final String SHORE = "riva";
    private static final String LAKE_PATH = "sentiero_lago";
    private static final String STONE = "pietra_custodi";
    private static final String CLEARING = "radura";

    private static final String MOON_WATER_PUZZLE = "puzzle_luna_acqua";
    private static final String MOON_WATER_MECHANISM = "varco_luna_acqua";

    /**
     * Costruisce il livello completo: luoghi, meccanismo, collegamenti, oggetti e personaggi.
     *
     * @param catalog catalogo in cui registrare gli oggetti del livello (serve al salvataggio)
     * @return il livello pronto, con luogo iniziale impostato
     */
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

    /** Crea i quattro luoghi del livello. */
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

    /**
     * Crea il varco tra i canneti, che si apre scegliendo la riva, e lo registra nella
     * pietra con {@link Level#addMechanism(String, Mechanism)}. Con Percezione sufficiente
     * compare un indizio; l'enigma resta risolvibile anche senza.
     */
    private void addPuzzleAndMechanism(Level level) {
        Map<String, String> options = new LinkedHashMap<>();
        options.put("monte", "La vetta delle montagne");
        options.put("centro", "Il centro del lago");
        options.put("riva", "La riva dove la luna si riflette");
        Puzzle moonWater = new ChoicePuzzle(MOON_WATER_PUZZLE,
                "\"Dove la luna incontra l'acqua\". Verso quale punto ti dirigi?", options, "riva");
        Mechanism gap = new Mechanism(MOON_WATER_MECHANISM, "Varco tra i canneti",
                "Un passaggio nascosto che si apre solo a chi comprende la frase dei Custodi.",
                new PuzzleUnlockCondition(moonWater))
                .withHint(new AttributeCondition(Attribute.PERCEZIONE, 1),
                        "I canneti si piegano tutti verso un punto della riva, dove l'acqua è più calma.");
        level.addMechanism(STONE, gap);
    }

    /** Collega i luoghi; l'uscita dalla pietra verso la radura è bloccata dal varco. */
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

    /** Colloca i due documenti del livello, registrandoli nel catalogo. */
    private void placeItems(Level level, ItemCatalog catalog) {
        place(level, catalog, LAKE_PATH, new DocumentItem("segno_elara", "Segno sul tronco",
                "Un segno inciso su un albero lungo il sentiero.",
                "Il simbolo dei Custodi della Luna, inciso con cura: sono gli ultimi segni lasciati da tua madre."));
        place(level, catalog, STONE, new DocumentItem("appunto_luna", "Appunto di Elara",
                "Un foglietto piegato ai piedi della pietra.",
                "\"Dove la luna incontra l'acqua, lì finisce la ricerca.\""));
    }

    /** Inserisce Elara nella radura; il suo dialogo finale completa la missione. */
    private void placeNpcs(Level level) {
        Location clearing = level.getPlaceById(CLEARING);
        clearing.addNPC(new NPC("elara", "Elara", "Tua madre, finalmente ritrovata.",
                clearing, LakeDialogues.elara()));
    }

    /** Registra l'oggetto nel catalogo e lo colloca nel luogo indicato. */
    private void place(Level level, ItemCatalog catalog, String locationId, Item item) {
        catalog.register(item);
        level.getPlaceById(locationId).addItem(item);
    }
}