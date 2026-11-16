package it.unicam.cs.mpgc.rpg130579.model.entity;

import it.unicam.cs.mpgc.rpg130579.model.interaction.AttributeCondition;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BonusItemTest {
    private final Player player = new Player("p", "P", "giocatore", new Location("l", "L", "luogo"));

    private BonusItem seal() {
        return new BonusItem("sigillo", "Sigillo", "Un sigillo.", Attribute.SAPIENZA, 1);
    }

    @Test
    void bonusRaisesEffectiveValueButNotTheBase() {
        player.addItem(seal());

        assertEquals(1, player.getAttributeBonus(Attribute.SAPIENZA));
        assertEquals(1, player.getAttributeValue(Attribute.SAPIENZA));
        assertEquals(0, player.getStats().get(Attribute.SAPIENZA));
        assertEquals(0, player.getAttributeValue(Attribute.PERSUASIONE));
    }

    @Test
    void bonusAddsToTheBaseValue() {
        player.getStats().increase(Attribute.SAPIENZA, 1);
        player.addItem(seal());
        assertEquals(2, player.getAttributeValue(Attribute.SAPIENZA));
    }

    @Test
    void bonusCountsForAttributeConditions() {
        AttributeCondition condition = new AttributeCondition(Attribute.SAPIENZA, 1);
        assertFalse(condition.isMetBy(player, new StoryProgress()));
        player.addItem(seal());
        assertTrue(condition.isMetBy(player, new StoryProgress()));
    }

    @Test
    void removingTheItemRemovesTheBonus() {
        BonusItem seal = seal();
        player.addItem(seal);
        player.removeItem(seal);
        assertEquals(0, player.getAttributeValue(Attribute.SAPIENZA));
    }

    @Test
    void invalidArgumentsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new BonusItem("i", "I", "d", null, 1));
        assertThrows(IllegalArgumentException.class, () -> new BonusItem("i", "I", "d", Attribute.SAPIENZA, 0));
    }

    @Test
    void examineMentionsTheBonus() {
        assertTrue(seal().examine().contains("+1 Sapienza"));
    }
}
