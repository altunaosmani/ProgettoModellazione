package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.persistence.JsonSaveRepository;
import it.unicam.cs.mpgc.rpg130579.persistence.SaveData;
import it.unicam.cs.mpgc.rpg130579.view.ActionResult;
import it.unicam.cs.mpgc.rpg130579.view.ItemView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static it.unicam.cs.mpgc.rpg130579.controller.Walkthrough.*;
import static org.junit.jupiter.api.Assertions.*;

class PersistenceTest {
    private Path file;
    private GameController c;

    @BeforeEach
    void setUp(@TempDir Path dir) {
        file = dir.resolve("save.json");
        c = newController(file);
    }

    private List<String> inventoryIds() {
        return c.inventory().stream().map(ItemView::id).toList();
    }

    @Test
    void saveDataSurvivesJsonRoundTrip() {
        JsonSaveRepository repository = new JsonSaveRepository(file);
        SaveData original = new SaveData("livello2", "cortile", List.of("livello1"),
                List.of("medaglione", "chiave_ardan"), List.of("puzzle_alberi"),
                List.of("sentiero_occhio"), Map.of("rowan_aiuto", "si"), false, false);
        repository.save(original);
        SaveData loaded = repository.load().orElseThrow();

        assertEquals("livello2", loaded.getCurrentLevelId());
        assertEquals("cortile", loaded.getCurrentLocationId());
        assertEquals(List.of("livello1"), loaded.getCompletedLevelIds());
        assertEquals(List.of("medaglione", "chiave_ardan"), loaded.getInventoryItemIds());
        assertEquals(List.of("puzzle_alberi"), loaded.getSolvedPuzzleIds());
        assertEquals(List.of("sentiero_occhio"), loaded.getUnlockedMechanismIds());
        assertEquals("si", loaded.getFlags().get("rowan_aiuto"));
        assertFalse(loaded.isGameEnded());
    }

    @Test
    void loadWithoutFileReturnsEmpty() {
        assertTrue(new JsonSaveRepository(file).load().isEmpty());
        assertFalse(c.hasSave());
        assertFalse(c.load().success());
    }

    @Test
    void corruptedFileIsReportedNotSwallowed() throws IOException {
        Files.writeString(file, "questo non è json");
        assertThrows(UncheckedIOException.class, () -> new JsonSaveRepository(file).load());
        assertFalse(c.load().success());
    }

    @Test
    void saveThenNewGameThenLoadRestoresProgress() {
        level1(c);
        ok(c.save());

        c.newGame();
        assertEquals("casa", c.currentLocation().id());
        assertEquals(List.of("medaglione"), inventoryIds());
        assertTrue(c.hasSave());

        ok(c.load());

        assertEquals("grotta", c.currentLocation().id());
        assertTrue(inventoryIds().contains("chiave_ardan"));
        assertTrue(inventoryIds().contains("pagina_diario"));
        assertTrue(c.status().levelCompleted());
    }

    @Test
    void loadedItemsAreNotDuplicatedInTheWorld() {
        level1(c);
        ok(c.save());
        c.newGame();
        ok(c.load());

        assertTrue(c.currentLocation().items().stream().noneMatch(i -> i.id().equals("chiave_ardan")));
        assertTrue(c.currentLocation().items().stream().noneMatch(i -> i.id().equals("pagina_diario")));
    }

    @Test
    void loadedMechanismsStayUnlocked() {
        level1(c);
        ok(c.save());
        c.newGame();
        ok(c.load());

        ok(c.move("santuario"));
        assertFalse(c.currentLocation().mechanisms().get(0).locked());
        ok(c.move("bivio"));
        ok(c.move("santuario"));
    }

    @Test
    void victoryStateIsRestored() {
        level1(c);
        level2(c, true);
        level3(c);
        ok(c.advance());
        ok(c.save());

        c.newGame();
        assertFalse(c.status().gameOver());
        ok(c.load());

        assertTrue(c.status().gameOver());
        assertTrue(c.status().won());
        assertTrue(c.ending().isPresent());
    }

    @Test
    void unsupportedVersionLeavesCurrentGameUntouched() throws IOException {
        ok(c.move("piazza"));
        Files.writeString(file, "{\"version\":99,\"currentLevelId\":\"livello1\",\"currentLocationId\":\"casa\"}");

        ActionResult result = c.load();

        assertFalse(result.success());
        assertEquals("piazza", c.currentLocation().id());
    }

    @Test
    void saveReferencingUnknownItemIsRejectedAndStateKept() throws IOException {
        ok(c.move("piazza"));
        Files.writeString(file, "{\"version\":1,\"currentLevelId\":\"livello1\",\"currentLocationId\":\"casa\","
                + "\"inventoryItemIds\":[\"oggetto_inesistente\"]}");

        assertFalse(c.load().success());
        assertEquals("piazza", c.currentLocation().id());
        assertEquals(List.of("medaglione"), inventoryIds());
    }
}
