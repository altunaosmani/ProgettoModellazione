package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.EnigmaPieceItem;
import it.unicam.cs.mpgc.rpg130579.model.entity.GenericItem;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SequencePuzzleTest {
    private final EnigmaPieceItem a = new EnigmaPieceItem("a", "A", "frammento", "La luna...", 0);
    private final EnigmaPieceItem b = new EnigmaPieceItem("b", "B", "frammento", "...riflette ciò...", 1);
    private final EnigmaPieceItem c = new EnigmaPieceItem("c", "C", "frammento", "...che gli occhi non vedono.", 2);
    // passati volutamente in ordine sparso: il puzzle deve ordinarli da solo
    private final SequencePuzzle puzzle = new SequencePuzzle("frase", "Ricomponi la frase", List.of(c, a, b));

    private Player playerWith(Object... items) {
        Player p = new Player("p", "P", "giocatore", new Location("l", "L", "luogo"));
        for (Object item : items) {
            if (item instanceof EnigmaPieceItem piece) p.addItem(piece);
            else p.addItem((GenericItem) item);
        }
        return p;
    }

    private PuzzleAnswer answer(String... ids) {
        return new PuzzleAnswer(List.of(ids));
    }

    @Test
    void correctOrderSolvesIt() {
        assertTrue(puzzle.isSolvedBy(answer("a", "b", "c"), playerWith(a, b, c)));
    }

    @Test
    void answerIsCaseInsensitiveAndTrimmed() {
        assertTrue(puzzle.isSolvedBy(answer("A", " b ", "C"), playerWith(a, b, c)));
    }

    @Test
    void wrongOrderFails() {
        assertFalse(puzzle.isSolvedBy(answer("b", "a", "c"), playerWith(a, b, c)));
    }

    @Test
    void wrongNumberOfValuesFails() {
        assertFalse(puzzle.isSolvedBy(answer("a", "b"), playerWith(a, b, c)));
        assertFalse(puzzle.isSolvedBy(PuzzleAnswer.empty(), playerWith(a, b, c)));
    }

    @Test
    void missingPieceInInventoryFails() {
        assertFalse(puzzle.isSolvedBy(answer("a", "b", "c"), playerWith(a, b)));
    }

    @Test
    void nullArgumentsFailInsteadOfThrowing() {
        assertFalse(puzzle.isSolvedBy(null, playerWith(a, b, c)));
        assertFalse(puzzle.isSolvedBy(answer("a", "b", "c"), null));
    }

    @Test
    void needsAtLeastTwoPieces() {
        assertThrows(IllegalArgumentException.class, () -> new SequencePuzzle("p", "d", List.of(a)));
    }

    @Test
    void duplicatedPositionsAreRejected() {
        EnigmaPieceItem clash = new EnigmaPieceItem("x", "X", "frammento", "boh", 0);
        assertThrows(IllegalArgumentException.class, () -> new SequencePuzzle("p", "d", List.of(a, clash)));
    }

    @Test
    void describesItselfForTheUi() {
        assertEquals(3, puzzle.answerSize());
        assertEquals(3, puzzle.getPieceCount());
    }

    @Test
    void choicesAreOnlyThePiecesThePlayerOwns() {
        EnigmaPieceItem foreign = new EnigmaPieceItem("x", "X", "frammento", "altro", 9);
        GenericItem other = new GenericItem("g", "Oggetto", "generico");

        Map<String, String> choices = puzzle.choices(playerWith(a, c, foreign, other));

        assertEquals(List.of("a", "c"), List.copyOf(choices.keySet()));
        assertEquals("La luna...", choices.get("a"));
    }
}
