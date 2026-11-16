package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DialogueSessionTest {
    private Player player;
    private StoryProgress progress;
    private DialogueContext context;
    private DialogueSession session;

    private Dialogue branching() {
        DialogueNode start = new DialogueNode("start", List.of("uno", "due"), List.of(
                new DialogueChoice("avanti", "fine", new SetFlagEffect("scelta", "si")),
                new DialogueChoice("esci", null)));
        DialogueNode end = new DialogueNode("fine", List.of("tre"));
        return new Dialogue("Test", "start", List.of(start, end));
    }

    private Dialogue gated() {
        DialogueNode start = new DialogueNode("start", List.of("ciao"), List.of(
                new DialogueChoice("normale", null),
                new DialogueChoice("persuasivo", null,
                        new ModifyAttributeEffect(Attribute.SAPIENZA, 1),
                        new AttributeCondition(Attribute.PERSUASIONE, 1))));
        return new Dialogue("Gated", "start", List.of(start));
    }

    private void readStartLines() {
        session.nextLine();
        session.nextLine();
    }

    @BeforeEach
    void setUp() {
        player = new Player("p", "P", "giocatore", new Location("l", "L", "luogo"));
        progress = new StoryProgress();
        context = new DialogueContext(player, progress);
        session = new DialogueSession(branching(), context);
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
        DialogueSession linear = new DialogueSession(new Dialogue("Orwen", List.of("a", "b")), context);
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

    @Test
    void gatedChoiceIsHiddenWithoutTheAttribute() {
        DialogueSession s = new DialogueSession(gated(), context);
        s.nextLine();
        assertEquals(1, s.getChoices().size());
        assertEquals("normale", s.getChoices().get(0).text());
    }

    @Test
    void gatedChoiceAppearsWithTheAttributeAndAppliesItsEffect() {
        player.getStats().increase(Attribute.PERSUASIONE, 1);
        DialogueSession s = new DialogueSession(gated(), context);
        s.nextLine();
        assertEquals(2, s.getChoices().size());

        s.choose(1);

        assertEquals(1, player.getStats().get(Attribute.SAPIENZA));
        assertTrue(s.isFinished());
    }

    @Test
    void nodeWithOnlyGatedChoicesIsRejected() {
        DialogueChoice gatedOnly = new DialogueChoice("x", null, DialogueEffect.NONE,
                new AttributeCondition(Attribute.SAPIENZA, 1));
        assertThrows(IllegalArgumentException.class, () -> new DialogueNode("n", List.of("a"), List.of(gatedOnly)));
    }

    @Test
    void compositeEffectAppliesAllEffects() {
        CompositeEffect effect = CompositeEffect.of(new SetFlagEffect("k", "v"),
                new ModifyAttributeEffect(Attribute.PERCEZIONE, 2));
        effect.apply(context);
        assertTrue(progress.hasFlag("k", "v"));
        assertEquals(2, player.getStats().get(Attribute.PERCEZIONE));
    }
}