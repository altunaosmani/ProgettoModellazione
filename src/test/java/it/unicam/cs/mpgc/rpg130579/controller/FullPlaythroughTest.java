package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.view.DialogueView;
import it.unicam.cs.mpgc.rpg130579.view.EndingView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static it.unicam.cs.mpgc.rpg130579.controller.Walkthrough.*;
import static org.junit.jupiter.api.Assertions.*;

class FullPlaythroughTest {
    private GameController c;

    @BeforeEach
    void setUp(@TempDir Path dir) {
        c = newController(dir.resolve("save.json"));
    }

    @Test
    void gameStartsAtHomeWithMedallion() {
        assertEquals("casa", c.currentLocation().id());
        assertEquals(1, c.inventory().size());
        assertEquals("medaglione", c.inventory().get(0).id());
        assertFalse(c.status().gameOver());
        assertFalse(c.status().levelCompleted());
    }

    @Test
    void wholeStoryIsPlayableAndEndsWithVictory() {
        level1(c);
        assertTrue(c.status().levelCompleted());
        level2(c, true);
        assertTrue(c.status().levelCompleted());
        level3(c);
        assertTrue(c.status().levelCompleted());
        assertFalse(c.status().gameOver());

        ok(c.advance());

        assertTrue(c.status().gameOver());
        assertTrue(c.status().won());
        EndingView ending = c.ending().orElseThrow();
        assertEquals("THE LOST ELF", ending.gameTitle());
        assertEquals("VITTORIA", ending.outcome());
    }

    @Test
    void levelOneIsNotCompletedBeforeTakingTheKey() {
        ok(c.move("piazza"));
        ok(c.move("sentiero"));
        assertFalse(c.status().levelCompleted());
        assertFalse(c.advance().success());
    }

    @Test
    void advanceMovesPlayerToNextLevelStart() {
        level1(c);
        ok(c.advance());
        assertEquals("ingresso", c.currentLocation().id());
    }

    @Test
    void lockedExitCannotBeCrossed() {
        ok(c.move("piazza"));
        ok(c.move("sentiero"));
        ok(c.move("bivio"));
        assertFalse(c.move("santuario").success());
        assertEquals("bivio", c.currentLocation().id());
    }

    @Test
    void wrongPuzzleAnswerDoesNotUnlock() {
        ok(c.move("piazza"));
        ok(c.move("sentiero"));
        ok(c.move("bivio"));
        assertFalse(c.unlock("sentiero_occhio", List.of("luna")).success());
        assertFalse(c.move("santuario").success());
        ok(c.unlock("sentiero_occhio", List.of("occhio")));
        ok(c.move("santuario"));
    }

    @Test
    void castleGateNeedsTheKey() {
        level1(c);
        ok(c.advance());
        assertTrue(c.currentLocation().mechanisms().get(0).locked());
        ok(c.unlock("cancello_ardan", List.of()));
        assertFalse(c.currentLocation().mechanisms().get(0).locked());
    }

    @Test
    void phrasePuzzleRequiresCorrectOrder() {
        level1(c);
        ok(c.advance());
        ok(c.unlock("cancello_ardan", List.of()));
        ok(c.move("cortile"));
        ok(c.move("biblioteca"));
        for (String piece : MOON_ORDER) ok(c.takeItem(piece));

        assertFalse(c.unlock("simbolo_luna", List.of("frammento_occhi", "frammento_riflette", "frammento_luna")).success());
        assertFalse(c.move("archivio").success());
        ok(c.unlock("simbolo_luna", MOON_ORDER));
        ok(c.move("archivio"));
    }

    @Test
    void castleLevelNeedsRowanDialogueToBeCompleted() {
        level1(c);
        level2(c, false);
        assertFalse(c.status().levelCompleted());

        ok(c.move("archivio"));
        ok(c.move("biblioteca"));
        ok(c.move("cortile"));
        DialogueView end = talk(c, "rowan", 0);

        assertTrue(end.finished());
        assertTrue(c.status().levelCompleted());
    }

    @Test
    void rowanDialogueHasChoicesThenFinishes() {
        level1(c);
        ok(c.advance());
        ok(c.unlock("cancello_ardan", List.of()));
        ok(c.move("cortile"));

        DialogueView start = c.talkTo("rowan");
        assertEquals("Rowan", start.speaker());
        assertEquals(2, start.choices().size());
        assertFalse(start.finished());

        DialogueView help = c.choose(0);
        assertTrue(help.choices().isEmpty());
        assertTrue(help.finished());
    }

    @Test
    void actionsAfterVictoryAreRejected() {
        level1(c);
        level2(c, true);
        level3(c);
        ok(c.advance());
        assertFalse(c.move("radura").success());
        assertFalse(c.takeItem("segno_elara").success());
    }

    @Test
    void takingUnknownItemFailsWithoutCrashing() {
        assertFalse(c.takeItem("non_esiste").success());
    }

    @Test
    void levelOneNeedsBothDiaryAndKey() {
        ok(c.move("piazza"));
        ok(c.move("sentiero"));
        ok(c.move("bivio"));
        ok(c.unlock("sentiero_occhio", List.of("occhio")));
        ok(c.move("santuario"));
        ok(c.unlock("statua_santuario", List.of()));
        ok(c.move("grotta"));
        ok(c.takeItem("chiave_ardan"));
        assertFalse(c.status().levelCompleted());
        ok(c.takeItem("pagina_diario"));
        assertTrue(c.status().levelCompleted());
    }
}