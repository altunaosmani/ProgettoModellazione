package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.entity.DocumentItem;
import it.unicam.cs.mpgc.rpg130579.model.entity.EnigmaPieceItem;
import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.entity.QuestNPC;
import it.unicam.cs.mpgc.rpg130579.model.interaction.AllOfCondition;
import it.unicam.cs.mpgc.rpg130579.model.interaction.FlagCondition;
import it.unicam.cs.mpgc.rpg130579.model.interaction.HasItemCondition;
import it.unicam.cs.mpgc.rpg130579.model.interaction.Puzzle;
import it.unicam.cs.mpgc.rpg130579.model.interaction.Quest;
import it.unicam.cs.mpgc.rpg130579.model.interaction.SequencePuzzle;
import it.unicam.cs.mpgc.rpg130579.model.world.Exit;
import it.unicam.cs.mpgc.rpg130579.model.world.KeyUnlockCondition;
import it.unicam.cs.mpgc.rpg130579.model.world.Level;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import it.unicam.cs.mpgc.rpg130579.model.world.Mechanism;
import it.unicam.cs.mpgc.rpg130579.model.world.PuzzleUnlockCondition;

import java.util.List;

public class CastleLevelBuilder implements LevelBuilder {
    private static final String GATE_AREA = "ingresso";
    private static final String COURTYARD = "cortile";
    private static final String LIBRARY = "biblioteca";
    private static final String ARCHIVE = "archivio";
    private static final String TOWER = "torre";

    private static final String MOON_PUZZLE = "puzzle_frase_luna";
    private static final String MOON_MECHANISM = "simbolo_luna";
    private static final String LAKE_MAP = "mappa_lago";

    @Override
    public Level build(ItemCatalog catalog) {
        Level level = new Level("livello2", "Il Castello di Ardan", "Il vecchio castello abbandonato: qui Elara ha lasciato le sue tracce.", new Quest("quest2", "Il segreto dei Custodi", "Parla con Rowan e scopri dove ha portato la ricerca di tua madre.", new AllOfCondition(List.of(new FlagCondition(CastleDialogues.ROWAN_HELP_FLAG, CastleDialogues.ROWAN_HELP_VALUE), new HasItemCondition(LAKE_MAP)))));
        List<EnigmaPieceItem> phrasePieces = createPhrasePieces();
        addLocations(level);
        addMechanisms(level, phrasePieces);
        connectLocations(level);
        placeItems(level, catalog, phrasePieces);
        placeNpcs(level);
        level.setStartLocation(GATE_AREA);
        return level;
    }

    private void addLocations(Level level) {
        level.addPlace(new Location(GATE_AREA, "Cancello del castello", "Il vecchio Castello di Ardan, ormai abbandonato. Un massiccio cancello di ferro " + "ti sbarra la strada."));
        level.addPlace(new Location(COURTYARD, "Cortile", "Un cortile silenzioso, invaso dalle erbacce. Un vecchio guardiano ti osserva."));
        level.addPlace(new Location(LIBRARY, "Biblioteca", "Scaffali alti fino al soffitto. Sul pavimento è inciso un simbolo a forma di luna, " + "ma sembra mancare qualcosa."));
        level.addPlace(new Location(ARCHIVE, "Archivio segreto", "Una stanza nascosta piena di carte. Molte portano la scrittura di Elara."));
        level.addPlace(new Location(TOWER, "Punto più alto del castello", "Dalla cima si vede tutta la valle. Su un tavolo c'è una pergamena arrotolata."));
    }

    private void addMechanisms(Level level, List<EnigmaPieceItem> phrasePieces) {
        Mechanism gate = new Mechanism(StoryIds.ARDAN_GATE, "Cancello di Ardan", "Un massiccio cancello con una serratura segnata dal simbolo dei Custodi.", new KeyUnlockCondition());
        level.addMechanism(gate);
        level.getPlaceById(GATE_AREA).addMechanism(gate);

        Puzzle phrase = new SequencePuzzle(MOON_PUZZLE, "Una pagina è stata strappata dal libro. Ricomponi la frase ordinando i tre frammenti.", phrasePieces);
        Mechanism moon = new Mechanism(MOON_MECHANISM, "Simbolo della luna", "Un simbolo di luna inciso sul pavimento della biblioteca.", new PuzzleUnlockCondition(phrase));
        level.addMechanism(moon);
        level.getPlaceById(LIBRARY).addMechanism(moon);
    }

    private void connectLocations(Level level) {
        Mechanism gate = level.getMechanismById(StoryIds.ARDAN_GATE);
        Mechanism moon = level.getMechanismById(MOON_MECHANISM);

        level.getPlaceById(GATE_AREA).addExit(new Exit(COURTYARD, "Entra nel castello", COURTYARD, gate));

        Location courtyard = level.getPlaceById(COURTYARD);
        courtyard.addExit(new Exit(GATE_AREA, "Torna al cancello", GATE_AREA));
        courtyard.addExit(new Exit(LIBRARY, "Entra nella biblioteca", LIBRARY));

        Location library = level.getPlaceById(LIBRARY);
        library.addExit(new Exit(COURTYARD, "Torna nel cortile", COURTYARD));
        library.addExit(new Exit(ARCHIVE, "Attraversa il passaggio segreto", ARCHIVE, moon));

        Location archive = level.getPlaceById(ARCHIVE);
        archive.addExit(new Exit(LIBRARY, "Torna in biblioteca", LIBRARY));
        archive.addExit(new Exit(TOWER, "Sali alla torre", TOWER));

        level.getPlaceById(TOWER).addExit(new Exit(ARCHIVE, "Scendi all'archivio", ARCHIVE));
    }

    private List<EnigmaPieceItem> createPhrasePieces() {
        return List.of(
                new EnigmaPieceItem("frammento_luna", "Frammento in \"Storia di Ardan\"", "Una frase scritta a margine di una pagina.", "La luna...", 0),
                new EnigmaPieceItem("frammento_riflette", "Frammento in \"Antichi regni\"", "Una frase scritta a margine di una pagina.", "...riflette ciò...", 1),
                new EnigmaPieceItem("frammento_occhi", "Frammento in \"Creature magiche\"", "Una frase scritta a margine di una pagina.", "...che gli occhi non vedono.", 2));
    }

    private void placeItems(Level level, ItemCatalog catalog, List<EnigmaPieceItem> phrasePieces) {
        place(level, catalog, LIBRARY, new DocumentItem("libro_ardan", "Storia di Ardan", "Un volume sulla storia del castello.", "Il Castello di Ardan fu per secoli un luogo di studio e di custodia. " + "Poi, un giorno, i suoi abitanti se ne andarono."));
        place(level, catalog, LIBRARY, new DocumentItem("libro_creature", "Creature magiche", "Un bestiario illustrato.", "Un elenco di creature dei boschi. Molte pagine sono rovinate dall'umidità."));
        place(level, catalog, LIBRARY, new DocumentItem("libro_regni", "Antichi regni", "Un libro sui regni elfici scomparsi.", "I regni degli elfi caddero uno dopo l'altro, ma alcuni segreti sopravvissero."));
        place(level, catalog, LIBRARY, new DocumentItem("libro_foresta", "La Foresta dei Sussurri", "Un libro sulla foresta che hai attraversato.", "Si dice che la foresta parli solo a chi è disposto ad ascoltare."));
        place(level, catalog, LIBRARY, new DocumentItem("libro_custodi", "I Custodi", "Un antico libro con un simbolo che conosci bene.", "I Custodi della Luna furono un antico ordine elfico. Il loro emblema è un occhio " + "circondato da tre rami. \"Il Custode non protegge un tesoro. " + "Protegge ciò che non deve essere trovato.\""));
        for (EnigmaPieceItem piece : phrasePieces) {
            place(level, catalog, LIBRARY, piece);
        }

        place(level, catalog, ARCHIVE, new DocumentItem("documenti_elara", "Documenti di Elara", "Fogli appartenuti a tua madre.", "Qualcuno sta cercando il segreto dei Custodi. Se resto a Tanusha, " + "metterò in pericolo mio figlio. Devo sparire e lasciare poche tracce."));
        place(level, catalog, ARCHIVE, new DocumentItem("lettera_eren", "Lettera per Eren", "Una lettera indirizzata a te.", "\"Eren, se un giorno troverai questa lettera, significa che sei riuscito a seguire " + "le mie tracce. Non ho mai smesso di pensare a te. Quando sarai pronto a " + "conoscere tutta la verità, cercami dove la luna incontra l'acqua.\""));

        place(level, catalog, TOWER, new DocumentItem(LAKE_MAP, "Mappa del Lago degli Specchi", "Un'antica mappa trovata nel punto più alto del castello.", "La mappa indica un luogo nascosto tra le montagne: il Lago degli Specchi."));
    }

    private void placeNpcs(Level level) {
        Location courtyard = level.getPlaceById(COURTYARD);
        courtyard.addNPC(new QuestNPC("rowan", "Rowan", "Un vecchio guardiano rimasto al castello per molti anni.", courtyard, CastleDialogues.rowanStranger(), StoryIds.MEDALLION, CastleDialogues.rowanRecognizes()));
    }

    private void place(Level level, ItemCatalog catalog, String locationId, Item item) {
        catalog.register(item);
        level.getPlaceById(locationId).addItem(item);
    }
}
