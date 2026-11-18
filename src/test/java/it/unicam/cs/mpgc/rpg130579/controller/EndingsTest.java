package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.view.ActionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static it.unicam.cs.mpgc.rpg130579.controller.Walkthrough.*;
import static org.junit.jupiter.api.Assertions.*;

class EndingsTest {
    private GameController c;

    @BeforeEach
    void setUp(@TempDir Path dir) {
        c = newController(dir.resolve("save.json"));
    }

    /**
     * Dal sentiero alla grotta, con diario e chiave.
     */
    private void pathToCave() {
        ok(c.move("bivio"));
        ok(c.unlock("sentiero_occhio", List.of("occhio")));
        ok(c.move("santuario"));
        ok(c.unlock("statua_santuario", List.of()));
        ok(c.move("grotta"));
        ok(c.takeItem("pagina_diario"));
        ok(c.takeItem("chiave_ardan"));
    }

    private void squareToCave() {
        ok(c.move("sentiero"));
        pathToCave();
    }

    private void finishGame() {
        level2(c, true);
        level3(c);
        ok(c.advance());
    }

    private String subtitle() {
        return c.ending().orElseThrow().subtitle();
    }

    private int value(String attribute) {
        return c.stats().values().get(attribute);
    }

    // ---------- Finali ----------

    @Test
    void defaultEndingWhenNoSpecialChoiceWasMade() {
        level1(c);
        finishGame();
        assertEquals("HAI TROVATO ELARA", subtitle());
        assertEquals("VITTORIA", c.ending().orElseThrow().outcome());
    }

    @Test
    void persuasionEnding() {
        ok(c.move("piazza"));
        talk(c, "mira", 0);
        squareToCave();
        finishGame();
        assertTrue(subtitle().contains("PAROLE GIUSTE"));
    }

    @Test
    void doubtEndingAfterBelievingAldren() {
        ok(c.move("piazza"));
        talk(c, "aldren", 1);
        squareToCave();
        finishGame();
        assertTrue(subtitle().contains("DUBBI"));
    }

    @Test
    void guardianEndingNeedsWisdomAndPersuasion() {
        ok(c.move("piazza"));
        talk(c, "mira", 0);   // +1 Persuasione
        talk(c, "aldren", 0); // crede a Orwen: +1 Sapienza (+1 con l'enigma della luna)
        squareToCave();
        finishGame();
        assertTrue(subtitle().contains("CUSTODI"));
    }

    @Test
    void endingIsStillAvailableAfterSaveAndLoad() {
        ok(c.move("piazza"));
        talk(c, "mira", 0);
        squareToCave();
        finishGame();
        ok(c.save());

        c.newGame();
        ok(c.load());

        assertTrue(subtitle().contains("PAROLE GIUSTE"));
    }

    // ---------- Oggetti con bonus ----------

    @Test
    void claspGivesPersuasionWhileCarried() {
        ok(c.move("piazza"));
        ok(c.move("sentiero"));
        ActionResult result = ok(c.takeItem("fermaglio_elara"));

        assertTrue(result.message().contains("+1 Persuasione"));
        assertEquals(1, value("Persuasione"));
        assertEquals(1, c.stats().bonuses().get("Persuasione"));
    }

    @Test
    void claspUnlocksTheExtraRowanChoice() {
        ok(c.move("piazza"));
        ok(c.move("sentiero"));
        ok(c.takeItem("fermaglio_elara"));
        pathToCave();
        ok(c.advance());
        ok(c.unlock("cancello_ardan", List.of()));
        ok(c.move("cortile"));

        assertEquals(3, c.talkTo("rowan").choices().size());
    }

    @Test
    void sealBonusAddsToTheBaseValue() {
        level1(c);
        level2(c, true);                 // l'enigma della luna dà +1 Sapienza
        ok(c.move("archivio"));
        ok(c.takeItem("sigillo_custodi"));
        assertEquals(2, value("Sapienza"));
    }

    // ---------- Contenuti condizionati ----------

    @Test
    void perceptionDetailAppearsOnlyWithPerception() {
        ok(c.move("piazza"));
        ok(c.move("sentiero"));
        ok(c.move("bivio"));
        assertTrue(c.currentLocation().details().isEmpty());
    }

    @Test
    void perceptionDetailAppearsWhenPerceptionIsHighEnough() {
        ok(c.move("piazza"));
        talk(c, "aldren", 1); // +1 Percezione
        ok(c.move("sentiero"));
        ok(c.move("bivio"));
        assertFalse(c.currentLocation().details().isEmpty());
    }

    @Test
    void wisdomHintIsHiddenWithoutWisdom() {
        level1(c);
        ok(c.advance());
        ok(c.unlock("cancello_ardan", List.of()));
        ok(c.move("cortile"));
        ok(c.move("biblioteca"));
        assertTrue(c.currentLocation().mechanisms().get(0).hint().isBlank());
    }

    @Test
    void wisdomHintAppearsWithWisdom() {
        ok(c.move("piazza"));
        talk(c, "aldren", 0); // +1 Sapienza
        squareToCave();
        ok(c.advance());
        ok(c.unlock("cancello_ardan", List.of()));
        ok(c.move("cortile"));
        ok(c.move("biblioteca"));
        assertFalse(c.currentLocation().mechanisms().get(0).hint().isBlank());
    }
}