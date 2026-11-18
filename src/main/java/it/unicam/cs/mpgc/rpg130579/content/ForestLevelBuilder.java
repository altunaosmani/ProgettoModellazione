package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.entity.*;
import it.unicam.cs.mpgc.rpg130579.model.interaction.*;
import it.unicam.cs.mpgc.rpg130579.model.world.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Costruttore del primo livello: <i>La Foresta dei Sussurri</i> (villaggio di Tanusha e dintorni).
 * Configura l'esplorazione iniziale, gli enigmi del bivio e del santuario e gli indizi su Elara.
 */
public class ForestLevelBuilder implements LevelBuilder {
    private static final String HOME = "casa";
    private static final String SQUARE = "piazza";
    private static final String PATH = "sentiero";
    private static final String FORK = "bivio";
    private static final String SHRINE = "santuario";
    private static final String CAVE = "grotta";

    private static final String TREES_PUZZLE = "puzzle_alberi";
    private static final String TREES_MECHANISM = "sentiero_occhio";
    private static final String STATUE_PUZZLE = "puzzle_statua";
    private static final String STATUE_MECHANISM = "statua_santuario";

    @Override
    public Level build(ItemCatalog catalog) {
        Level level = new Level("livello1", "La Foresta dei Sussurri", "Il medaglione si è aperto: inizia la ricerca di Elara.", new Quest("quest1", "Segui le tracce di Elara", "Scopri dove è andata tua madre", new HasItemCondition(StoryIds.ARDAN_KEY)));
        addLocations(level);
        addPuzzlesAndMechanisms(level);
        connectLocations(level);
        placeItems(level, catalog);
        placeNpcs(level);

        level.setStartLocation(HOME);
        return level;
    }

    private void addLocations(Level level) {
        level.addPlace(new Location(HOME, "Casa", "La tua casa a Tanusha. Il medaglione si è appena aperto."));
        level.addPlace(new Location(SQUARE, "Piazza del villaggio", "Il centro di Tanusha, dove gli abitanti si incontrano."));
        level.addPlace(new Location(PATH, "Sentiero nella foresta", "Un sentiero tra gli alberi. Sul terreno ci sono vecchie impronte."));
        level.addPlace(new Location(FORK, "Il bivio", "Il sentiero si divide in tre direzioni. Su tre alberi sono incisi una luna, " + "un sole e un occhio."));
        level.addPlace(new Location(SHRINE, "Antico santuario", "Al centro c'è una statua con il simbolo del medaglione. Sull'iscrizione si legge: " + "\"Solo chi cerca la verità può aprire il sentiero.\""));
        level.addPlace(new Location(CAVE, "La grotta", "Una piccola grotta nascosta. Su una parete è incisa un'iscrizione: " + "\"Ciò che cerchi è custodito dove il passato non è mai morto.\""));
        level.getPlaceById(FORK).addDetail(new ConditionalText(new AttributeCondition(Attribute.PERCEZIONE, 1), "Noti che l'erba è calpestata solo lungo il sentiero dell'occhio: qualcuno è passato di qui, e più di una volta."));
    }

    private void addPuzzlesAndMechanisms(Level level) {
        // Alberi
        Map<String, String> symbols = new LinkedHashMap<>();
        symbols.put("luna", "Il sentiero della luna");
        symbols.put("sole", "Il sentiero del sole");
        symbols.put("occhio", "Il sentiero dell'occhio");
        Puzzle trees = new ChoicePuzzle(TREES_PUZZLE, "Quale sentiero scegli? Uno dei simboli è lo stesso del tuo medaglione.", symbols, "occhio");
        Mechanism treesMechanism = new Mechanism(TREES_MECHANISM, "Sentiero dell'occhio",
                "Il sentiero che segue il simbolo del medaglione.", new PuzzleUnlockCondition(trees))
                .withReward(Attribute.PERCEZIONE, 1);
        level.addMechanism(treesMechanism);
        level.getPlaceById(FORK).addMechanism(treesMechanism);
        Puzzle statue = new ItemPuzzle(STATUE_PUZZLE, "Avvicina il medaglione alla statua.", StoryIds.MEDALLION);
        Mechanism statueMechanism = new Mechanism(STATUE_MECHANISM, "Statua del santuario", "Una statua con lo stesso simbolo del medaglione.", new PuzzleUnlockCondition(statue));
        level.addMechanism(statueMechanism);
        level.getPlaceById(SHRINE).addMechanism(statueMechanism);
    }

    private void connectLocations(Level level) {
        Mechanism treesGate = level.getMechanismById(TREES_MECHANISM);
        Mechanism statueGate = level.getMechanismById(STATUE_MECHANISM);

        level.getPlaceById(HOME).addExit(new Exit(SQUARE, "Vai alla piazza", SQUARE));

        Location square = level.getPlaceById(SQUARE);
        square.addExit(new Exit(HOME, "Torna a casa", HOME));
        square.addExit(new Exit(PATH, "Entra nella Foresta dei Sussurri", PATH));

        Location path = level.getPlaceById(PATH);
        path.addExit(new Exit(SQUARE, "Torna al villaggio", SQUARE));
        path.addExit(new Exit(FORK, "Prosegui lungo il sentiero", FORK));

        Location fork = level.getPlaceById(FORK);
        fork.addExit(new Exit(PATH, "Torna indietro", PATH));
        fork.addExit(new Exit(SHRINE, "Segui il simbolo dell'occhio", SHRINE, treesGate));

        Location shrine = level.getPlaceById(SHRINE);
        shrine.addExit(new Exit(FORK, "Torna al bivio", FORK));
        shrine.addExit(new Exit(CAVE, "Entra nella porta nascosta", CAVE, statueGate));

        level.getPlaceById(CAVE).addExit(new Exit(SHRINE, "Torna al santuario", SHRINE));
    }

    private void placeItems(Level level, ItemCatalog catalog) {
        DocumentItem book = new DocumentItem("libro_madre", "Vecchio libro", "Un libro appartenuto a tua madre.", "Sul retro dell'immagine di Elara è scritto: \"Tanusha non è il posto da cui sono partita.\"");
        GenericItem cloth = new GenericItem("stoffa_blu", "Pezzo di stoffa blu", "Un pezzo di stoffa blu: apparteneva al mantello di tua madre.");
        DocumentItem diary = new DocumentItem("pagina_diario", "Pagina del diario", "Una pagina del diario di Elara.", "\"Se qualcuno troverà queste parole, significa che sono riuscita a lasciare Tanusha. " + "Non posso ancora tornare. Devo trovare ciò che è stato nascosto ad Ardan.\"");
        KeyItem key = new KeyItem(StoryIds.ARDAN_KEY, "Vecchia chiave", "Una vecchia chiave con lo stesso simbolo del medaglione.", StoryIds.ARDAN_GATE);
        for (var item : new it.unicam.cs.mpgc.rpg130579.model.entity.Item[]{book, cloth, diary, key}) {
            catalog.register(item);
        }
        level.getPlaceById(HOME).addItem(book);
        level.getPlaceById(PATH).addItem(cloth);
        level.getPlaceById(CAVE).addItem(diary);
        level.getPlaceById(CAVE).addItem(key);
        BonusItem clasp = new BonusItem("fermaglio_elara", "Fermaglio d'argento", "Un fermaglio d'argento con un piccolo occhio inciso. Forse apparteneva a tua madre.", Attribute.PERSUASIONE, 1);
        catalog.register(clasp);
        level.getPlaceById(PATH).addItem(clasp);
    }

    private void placeNpcs(Level level) {
        Location square = level.getPlaceById(SQUARE);
        addNpc(level, SQUARE, "orwen", "Orwen", "Un vecchio bibliotecario che conosceva Elara.", ForestDialogues.orwen());
        square.addNPC(new QuestNPC("mira", "Mira", "Una mercante, amica di tua madre.", square,
                ForestDialogues.mira(),
                new FlagCondition(ForestDialogues.MIRA_TALKED_FLAG, ForestDialogues.DONE),
                ForestDialogues.miraAfter()));
        square.addNPC(new QuestNPC("aldren", "Aldren", "Una guardia del villaggio che evita l'argomento.", square,
                ForestDialogues.aldren(),
                new FlagCondition(ForestDialogues.ALDREN_TALKED_FLAG, ForestDialogues.DONE),
                ForestDialogues.aldrenAfter()));
    }

    private void addNpc(Level level, String locationId, String id, String name, String description, Dialogue dialogue) {
        Location location = level.getPlaceById(locationId);
        location.addNPC(new NPC(id, name, description, location, dialogue));
    }
}