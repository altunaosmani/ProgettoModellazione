package it.unicam.cs.mpgc.rpg130579.model.world;

import it.unicam.cs.mpgc.rpg130579.model.entity.KeyItem;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.interaction.ChoicePuzzle;
import it.unicam.cs.mpgc.rpg130579.model.interaction.PuzzleAnswer;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MechanismTest {
    private final Player player = new Player("p", "P", "giocatore", new Location("l", "L", "luogo"));

    private Mechanism gate() {
        return new Mechanism("cancello", "Cancello", "Un cancello", new KeyUnlockCondition());
    }

    private Mechanism riddle() {
        Map<String, String> options = new LinkedHashMap<>();
        options.put("monte", "Il monte");
        options.put("riva", "La riva");
        return new Mechanism("varco", "Varco", "Un varco",
                new PuzzleUnlockCondition(new ChoicePuzzle("p", "Dove?", options, "riva")));
    }

    @Test
    void startsLocked() {
        assertTrue(gate().isLocked());
        assertFalse(gate().isUnlocked());
    }

    @Test
    void keyMechanismStaysLockedWithoutTheRightKey() {
        Mechanism gate = gate();
        assertFalse(gate.tryUnlock(player));

        player.addItem(new KeyItem("chiave_altra", "Altra chiave", "d", "altro_cancello"));
        assertFalse(gate.tryUnlock(player));
        assertTrue(gate.isLocked());
    }

    @Test
    void rightKeyUnlocksAndItStaysUnlocked() {
        Mechanism gate = gate();
        player.addItem(new KeyItem("chiave", "Chiave", "d", "CANCELLO"));

        assertTrue(gate.tryUnlock(player));
        assertTrue(gate.isUnlocked());
        player.removeItem(player.getInventory().get(0));
        assertTrue(gate.tryUnlock(player)); // una volta aperto resta aperto
    }

    @Test
    void puzzleMechanismNeedsTheCorrectAnswer() {
        Mechanism riddle = riddle();

        assertFalse(riddle.tryUnlock(player, new PuzzleAnswer(List.of("monte"))));
        assertFalse(riddle.tryUnlock(player));
        assertTrue(riddle.tryUnlock(player, new PuzzleAnswer(List.of("riva"))));
    }

    @Test
    void exposesItsRequiredPuzzleOnlyWhenThereIsOne() {
        assertTrue(gate().getRequiredPuzzle().isEmpty());
        assertTrue(riddle().getRequiredPuzzle().isPresent());
    }

    @Test
    void restoreUnlockedOpensWithoutConditions() {
        Mechanism gate = gate();
        gate.restoreUnlocked();
        assertTrue(gate.isUnlocked());
    }

    @Test
    void exitFollowsItsBarrier() {
        Mechanism gate = gate();
        Exit gated = new Exit("cortile", "Entra", "cortile", gate);
        Exit free = new Exit("casa", "Torna", "casa");

        assertFalse(gated.isPassable());
        assertTrue(free.isPassable());

        gate.restoreUnlocked();
        assertTrue(gated.isPassable());
    }

    @Test
    void locationRejectsDuplicateExitsAndLooksThemUpIgnoringCase() {
        Location location = new Location("l", "L", "luogo");
        assertTrue(location.addExit(new Exit("Nord", "Vai a nord", "n")));
        assertFalse(location.addExit(new Exit("nord", "Altro", "x")));
        assertNotNull(location.getExitById("NORD"));
        assertNull(location.getExitById("sud"));
    }
}
