package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.content.GameFactory;
import it.unicam.cs.mpgc.rpg130579.persistence.JsonSaveRepository;
import it.unicam.cs.mpgc.rpg130579.view.ActionResult;
import it.unicam.cs.mpgc.rpg130579.view.DialogueView;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Percorso giocabile dei tre livelli, usato da più test.
 */
final class Walkthrough {
    static final List<String> MOON_ORDER = List.of("frammento_luna", "frammento_riflette", "frammento_occhi");

    private Walkthrough() {
    }

    static GameController newController(Path saveFile) {
        return new DefaultGameController(new GameFactory()::newGame, new JsonSaveRepository(saveFile));
    }

    static ActionResult ok(ActionResult result) {
        assertTrue(result.success(), result.message());
        return result;
    }

    /**
     * Parla con un NPC facendo le scelte indicate (indici); restituisce l'ultima vista.
     */
    static DialogueView talk(GameController c, String npcId, int... choices) {
        DialogueView view = c.talkTo(npcId);
        for (int choice : choices) view = c.choose(choice);
        return view;
    }

    /**
     * Dalla casa fino alla grotta, con diario e chiave in inventario. Il livello risulta completato.
     */
    static void level1(GameController c) {
        ok(c.move("piazza"));
        ok(c.move("sentiero"));
        ok(c.takeItem("stoffa_blu"));
        ok(c.move("bivio"));
        ok(c.unlock("sentiero_occhio", List.of("occhio")));
        ok(c.move("santuario"));
        ok(c.unlock("statua_santuario", List.of()));
        ok(c.move("grotta"));
        ok(c.takeItem("pagina_diario"));
        ok(c.takeItem("chiave_ardan"));
    }

    /**
     * Dal cancello alla torre. Se talkToRowan è false salta il dialogo che completa la missione.
     */
    static void level2(GameController c, boolean talkToRowan) {
        ok(c.advance());
        ok(c.unlock("cancello_ardan", List.of()));
        ok(c.move("cortile"));
        if (talkToRowan) talk(c, "rowan", 0);
        ok(c.move("biblioteca"));
        for (String piece : MOON_ORDER) ok(c.takeItem(piece));
        ok(c.unlock("simbolo_luna", MOON_ORDER));
        ok(c.move("archivio"));
        ok(c.move("torre"));
        ok(c.takeItem("mappa_lago"));
    }

    /**
     * Dalla riva del lago al dialogo finale con Elara.
     */
    static void level3(GameController c) {
        ok(c.advance());
        ok(c.move("sentiero_lago"));
        ok(c.move("pietra_custodi"));
        ok(c.unlock("varco_luna_acqua", List.of("riva")));
        ok(c.move("radura"));
        talk(c, "elara", 0, 0, 0);
    }
}
