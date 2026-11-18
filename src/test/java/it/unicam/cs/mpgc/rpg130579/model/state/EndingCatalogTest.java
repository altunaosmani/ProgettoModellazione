package it.unicam.cs.mpgc.rpg130579.model.state;

import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.interaction.QuestCondition;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EndingCatalogTest {
    private final Player player = new Player("p", "P", "giocatore", new Location("l", "L", "luogo"));
    private final StoryProgress progress = new StoryProgress();

    private static EndingScreen ending(String subtitle) {
        return new EndingScreen("T", "H", subtitle, "M", "VITTORIA");
    }

    @Test
    void usesTheFallbackWhenNoVariantMatches() {
        EndingCatalog catalog = new EndingCatalog(ending("base"),
                List.of(new EndingCatalog.Variant((p, s) -> false, ending("mai"))));
        assertEquals("base", catalog.select(player, progress).subtitle());
    }

    @Test
    void firstMatchingVariantWins() {
        EndingCatalog catalog = new EndingCatalog(ending("base"), List.of(
                new EndingCatalog.Variant((p, s) -> false, ending("no")),
                new EndingCatalog.Variant(QuestCondition.ALWAYS, ending("primo")),
                new EndingCatalog.Variant(QuestCondition.ALWAYS, ending("secondo"))));
        assertEquals("primo", catalog.select(player, progress).subtitle());
    }

    @Test
    void worksWithoutVariants() {
        assertEquals("base", new EndingCatalog(ending("base")).select(player, progress).subtitle());
    }

    @Test
    void invalidArgumentsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new EndingCatalog(null));
        assertThrows(IllegalArgumentException.class, () -> new EndingCatalog.Variant(null, ending("x")));
        assertThrows(IllegalArgumentException.class, () -> new EndingCatalog.Variant(QuestCondition.ALWAYS, null));
    }
}
