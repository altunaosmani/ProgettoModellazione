package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.view.DialogueView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static it.unicam.cs.mpgc.rpg130579.controller.Walkthrough.*;
import static org.junit.jupiter.api.Assertions.*;

class StoryBranchesTest {
    private static final String DOUBT_LINE = "non ero mai entrata";

    private Path file;
    private GameController c;

    @BeforeEach
    void setUp(@TempDir Path dir) {
        file = dir.resolve("save.json");
        c = newController(file);
    }

    /**
     * Gioca fino alla radura e apre il dialogo con Elara, senza proseguire.
     */
    private DialogueView meetElara(boolean believeAldren) {
        ok(c.move("piazza"));
        if (believeAldren) talk(c, "aldren", 1);
        ok(c.move("sentiero"));
        ok(c.move("bivio"));
        ok(c.unlock("sentiero_occhio", List.of("occhio")));
        ok(c.move("santuario"));
        ok(c.unlock("statua_santuario", List.of()));
        ok(c.move("grotta"));
        ok(c.takeItem("pagina_diario"));
        ok(c.takeItem("chiave_ardan"));
        level2(c, true);
        ok(c.advance());
        ok(c.move("sentiero_lago"));
        ok(c.move("pietra_custodi"));
        ok(c.unlock("varco_luna_acqua", List.of("riva")));
        ok(c.move("radura"));
        return c.talkTo("elara");
    }

    @Test
    void elaraMentionsTheDoubtIfYouBelievedAldren() {
        DialogueView start = meetElara(true);
        assertTrue(start.lines().stream().anyMatch(l -> l.contains(DOUBT_LINE)));
    }

    @Test
    void elaraHasNoDoubtLineOtherwise() {
        DialogueView start = meetElara(false);
        assertTrue(start.lines().stream().noneMatch(l -> l.contains(DOUBT_LINE)));
    }

    @Test
    void doubtVariantStillCompletesTheLevel() {
        meetElara(true);
        c.choose(0);
        c.choose(0);
        assertTrue(c.choose(0).finished());
        assertTrue(c.status().levelCompleted());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "\"flags\":{\"k\":\"\"}",
            "\"solvedPuzzleIds\":[\" \"]",
            "\"inventoryItemIds\":[\"medaglione\",\"medaglione\"]",
            "\"inventoryItemIds\":[\"\"]"
    })
    void invalidSaveIsRejectedAndCurrentGameIsKept(String fragment) throws IOException {
        ok(c.move("piazza"));
        Files.writeString(file,
                "{\"version\":2,\"currentLevelId\":\"livello1\",\"currentLocationId\":\"casa\"," + fragment + "}");

        assertFalse(c.load().success());
        assertEquals("piazza", c.currentLocation().id());
    }
}
