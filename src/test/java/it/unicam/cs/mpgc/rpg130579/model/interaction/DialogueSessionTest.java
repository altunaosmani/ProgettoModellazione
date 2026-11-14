package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DialogueSessionTest {
    private StoryProgress progress;
    private DialogueSession session;

    private Dialogue branching() {
        DialogueNode start = new DialogueNode("start", List.of("uno", "due"), List.of(
                new DialogueChoice("avanti", "fine", new SetFlagEffect("scelta", "si")),
                new DialogueChoice("esci", null)));
        DialogueNode end = new DialogueNode("fine", List.of("tre"));
        return new Dialogue("Test", "start", List.of(start, end));
    }

    private void readStartLines() {
        session.nextLine();
        session.nextLine();
    }

    @BeforeEach
    void setUp() {
        progress = new StoryProgress();
        session = new DialogueSession(branching(), progress);
    }

    @Test
    void linesComeInOrderAndChoicesOnlyAfterThem() {
        assertFalse(session.awaitsChoice());
        assertEquals("uno", session.nextLine());
        assertFalse(session.awaitsChoice());
        assertEquals("due", session.nextLine());
        assertTrue(session.awaitsChoice());
        assertEquals(2, session.getChoices().size());
        assertFalse(session.isFinished());
    }

    @Test
    void choosingMovesOnAndAppliesTheEffect() {
        readStartLines();
        session.choose(0);

        assertTrue(progress.hasFlag("scelta", "si"));
        assertEquals("tre", session.nextLine());
        assertTrue(session.isFinished());
    }

    @Test
    void choiceWithoutNextNodeEndsTheDialogue() {
        readStartLines();
        session.choose(1);

        assertTrue(session.isFinished());
        assertFalse(progress.hasFlag("scelta", "si"));
        assertFalse(session.hasNextLine());
    }

    @Test
    void invalidChoiceIndexIsRejected() {
        readStartLines();
        assertThrows(IllegalArgumentException.class, () -> session.choose(5));
        assertThrows(IllegalArgumentException.class, () -> session.choose(-1));
    }

    @Test
    void cannotChooseBeforeReadingAllLines() {
        assertThrows(IllegalStateException.class, () -> session.choose(0));
    }

    @Test
    void cannotReadPastTheLastLine() {
        readStartLines();
        session.choose(1);
        assertThrows(IllegalStateException.class, () -> session.nextLine());
    }

    @Test
    void linearDialogueFinishesAfterItsLines() {
        DialogueSession linear = new DialogueSession(new Dialogue("Orwen", List.of("a", "b")), progress);
        assertFalse(linear.isFinished());
        linear.nextLine();
        linear.nextLine();
        assertTrue(linear.isFinished());
        assertFalse(linear.awaitsChoice());
    }

    @Test
    void dialogueWithDanglingChoiceIsRejectedAtCreation() {
        DialogueNode start = new DialogueNode("start", List.of("ciao"),
                List.of(new DialogueChoice("vai", "nodo_che_non_esiste")));
        assertThrows(IllegalArgumentException.class, () -> new Dialogue("X", "start", List.of(start)));
    }

    @Test
    void duplicateNodeIdsAreRejected() {
        DialogueNode one = new DialogueNode("start", List.of("a"));
        DialogueNode two = new DialogueNode("start", List.of("b"));
        assertThrows(IllegalArgumentException.class, () -> new Dialogue("X", "start", List.of(one, two)));
    }
}
