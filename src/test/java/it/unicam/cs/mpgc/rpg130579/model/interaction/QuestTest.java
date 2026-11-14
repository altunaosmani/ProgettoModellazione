package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.GenericItem;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestTest {
    private final Player player = new Player("p", "P", "giocatore", new Location("l", "L", "luogo"));
    private final StoryProgress progress = new StoryProgress();

    @Test
    void allOfConditionNeedsEveryPart() {
        Quest quest = new Quest("q", "Quest", "descrizione",
                new AllOfCondition(List.of(new FlagCondition("rowan_aiuto", "si"), new HasItemCondition("mappa"))));
        assertFalse(quest.isCompletable(player, progress));

        progress.setFlag("rowan_aiuto", "si");
        assertFalse(quest.isCompletable(player, progress));

        player.addItem(new GenericItem("mappa", "Mappa", "d"));
        assertTrue(quest.isCompletable(player, progress));
    }

    @Test
    void completedQuestIsNoLongerCompletable() {
        Quest quest = new Quest("q", "Quest", "descrizione", QuestCondition.ALWAYS);
        assertTrue(quest.isCompletable(player, progress));

        quest.complete();

        assertTrue(quest.isCompleted());
        assertFalse(quest.isCompletable(player, progress));
    }

    @Test
    void flagValuesAreComparedIgnoringCase() {
        progress.setFlag("Credenza", "Orwen");
        assertTrue(progress.hasFlag("credenza", "orwen"));
        assertFalse(progress.hasFlag("credenza", "aldren"));
    }

    @Test
    void puzzleSolvedConditionReadsStoryProgress() {
        PuzzleSolvedCondition condition = new PuzzleSolvedCondition("alberi");
        assertFalse(condition.isMetBy(player, progress));
        progress.markPuzzleSolved("alberi");
        assertTrue(condition.isMetBy(player, progress));
    }
}
