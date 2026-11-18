package it.unicam.cs.mpgc.rpg130579.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static it.unicam.cs.mpgc.rpg130579.controller.Walkthrough.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RpgAttributesTest {
    private GameController c;

    @BeforeEach
    void setUp(@TempDir Path dir) {
        c = newController(dir.resolve("save.json"));
    }

    private int stat(String name) {
        return c.stats().values().get(name);
    }

    @Test
    void statsStartAtZero() {
        assertEquals(0, stat("Sapienza"));
        assertEquals(0, stat("Persuasione"));
        assertEquals(0, stat("Percezione"));
    }

    @Test
    void solvingAPuzzleGivesItsBonusOnlyOnce() {
        ok(c.move("piazza"));
        ok(c.move("sentiero"));
        ok(c.move("bivio"));
        assertTrue(c.unlock("sentiero_occhio", List.of("occhio")).message().contains("+1 Percezione"));
        assertEquals(1, stat("Percezione"));

        ok(c.unlock("sentiero_occhio", List.of("occhio")));
        assertEquals(1, stat("Percezione"));
    }

    @Test
    void miraGivesPersuasionOnlyOnce() {
        ok(c.move("piazza"));
        talk(c, "mira", 0);
        assertEquals(1, stat("Persuasione"));

        DialogueViewAssert.finishedWithoutChoices(c.talkTo("mira"));
        assertEquals(1, stat("Persuasione"));
    }

    @Test
    void persuasionAddsAnExtraRowanChoice() {
        ok(c.move("piazza"));
        talk(c, "mira", 0);
        // di nuovo fino alla grotta e al castello
        ok(c.move("sentiero"));
        ok(c.move("bivio"));
        ok(c.unlock("sentiero_occhio", List.of("occhio")));
        ok(c.move("santuario"));
        ok(c.unlock("statua_santuario", List.of()));
        ok(c.move("grotta"));
        ok(c.takeItem("pagina_diario"));
        ok(c.takeItem("chiave_ardan"));
        ok(c.advance());
        ok(c.unlock("cancello_ardan", List.of()));
        ok(c.move("cortile"));

        assertEquals(3, c.talkTo("rowan").choices().size());
        assertTrue(c.choose(2).finished());
        assertTrue(c.currentLocation().id().equals("cortile"));
    }

    @Test
    void withoutPersuasionRowanHasTwoChoices() {
        level1(c);
        ok(c.advance());
        ok(c.unlock("cancello_ardan", List.of()));
        ok(c.move("cortile"));
        assertEquals(2, c.talkTo("rowan").choices().size());
    }

    @Test
    void aldrenBonusIsGivenOnlyOnce() {
        ok(c.move("piazza"));
        talk(c, "aldren", 0);
        assertEquals(1, stat("Sapienza"));
        assertTrue(c.talkTo("aldren").finished());
        assertEquals(1, stat("Sapienza"));
    }

    @Test
    void storyStillEndsWithVictoryAfterBelievingAldren() {
        ok(c.move("piazza"));
        talk(c, "aldren", 1); // crede ad Aldren: Elara avrà una battuta in più
        ok(c.move("sentiero"));
        ok(c.move("bivio"));
        ok(c.unlock("sentiero_occhio", List.of("occhio")));
        ok(c.move("santuario"));
        ok(c.unlock("statua_santuario", List.of()));
        ok(c.move("grotta"));
        ok(c.takeItem("pagina_diario"));
        ok(c.takeItem("chiave_ardan"));
        level2(c, true);
        level3(c);
        ok(c.advance());
        assertTrue(c.status().won());
    }

    private static final class DialogueViewAssert {
        static void finishedWithoutChoices(it.unicam.cs.mpgc.rpg130579.view.DialogueView v) {
            assertTrue(v.finished());
            assertTrue(v.choices().isEmpty());
        }
    }
}