package it.unicam.cs.mpgc.rpg130579.persistence;

import it.unicam.cs.mpgc.rpg130579.content.GameFactory;
import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.state.GameState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SaveContributorTest {
    @Test
    void extensionSectionRoundTrips(@TempDir Path dir) {
        List<String> restored = new ArrayList<>();
        SaveContributor demo = new SaveContributor() {
            @Override
            public String key() {
                return "demo";
            }

            @Override
            public Map<String, String> capture(GameState state) {
                return Map.of("k", "v");
            }

            @Override
            public Runnable prepareRestore(GameState state, ItemCatalog catalog, Map<String, String> saved) {
                return () -> restored.add(saved.get("k"));
            }
        };
        SaveService service = new SaveService(new JsonSaveRepository(dir.resolve("s.json")), List.of(demo));

        service.save(new GameFactory().newGame().state());
        var fresh = new GameFactory().newGame();

        assertTrue(service.load(fresh.state(), fresh.catalog()));
        assertEquals(List.of("v"), restored);
    }

    @Test
    void duplicateContributorKeysAreRejected(@TempDir Path dir) {
        SaveContributor a = new SaveContributor() {
            public String key() { return "x"; }
            public Map<String, String> capture(GameState s) { return Map.of(); }
            public Runnable prepareRestore(GameState s, ItemCatalog c, Map<String, String> m) { return () -> { }; }
        };
        assertThrows(IllegalArgumentException.class,
                () -> new SaveService(new JsonSaveRepository(dir.resolve("s.json")), List.of(a, a)));
    }
}