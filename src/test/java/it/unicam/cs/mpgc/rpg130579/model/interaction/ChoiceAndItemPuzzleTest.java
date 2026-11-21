package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.GenericItem;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ChoiceAndItemPuzzleTest {
    private final Player player = new Player("p", "P", "giocatore", new Location("l", "L", "luogo"));

    private ChoicePuzzle choice() {
        Map<String, String> options = new LinkedHashMap<>();
        options.put("luna", "Sentiero della luna");
        options.put("occhio", "Sentiero dell'occhio");
        return new ChoicePuzzle("alberi", "Quale sentiero?", options, "occhio");
    }

    @Test
    void correctOptionSolvesChoicePuzzleIgnoringCase() {
        assertTrue(choice().isSolvedBy(new PuzzleAnswer(List.of("OCCHIO")), player));
    }

    @Test
    void wrongOrMultipleOptionsFail() {
        assertFalse(choice().isSolvedBy(new PuzzleAnswer(List.of("luna")), player));
        assertFalse(choice().isSolvedBy(new PuzzleAnswer(List.of("luna", "occhio")), player));
        assertFalse(choice().isSolvedBy(PuzzleAnswer.empty(), player));
    }

    @Test
    void choicePuzzleExposesOptionsInInsertionOrder() {
        ChoicePuzzle puzzle = choice();
        assertEquals(1, puzzle.answerSize());
        assertEquals(List.of("luna", "occhio"), List.copyOf(puzzle.choices(player).keySet()));
    }

    @Test
    void correctOptionMustBeAmongTheOptions() {
        assertThrows(IllegalArgumentException.class,
                () -> new ChoicePuzzle("p", "d", Map.of("a", "A", "b", "B"), "z"));
    }

    @Test
    void itemPuzzleIsSolvedOnlyByOwningTheItem() {
        ItemPuzzle puzzle = new ItemPuzzle("statua", "Avvicina il medaglione", "medaglione");
        assertFalse(puzzle.isSolvedBy(PuzzleAnswer.empty(), player));

        player.addItem(new GenericItem("medaglione", "Medaglione", "d"));

        assertTrue(puzzle.isSolvedBy(PuzzleAnswer.empty(), player));
        assertEquals(0, puzzle.answerSize());
    }
}
