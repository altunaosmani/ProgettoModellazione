package it.unicam.cs.mpgc.rpg130579.model.world;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.interaction.AttributeCondition;
import it.unicam.cs.mpgc.rpg130579.model.interaction.ConditionalText;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConditionalContentTest {
    private final Player player = new Player("p", "P", "giocatore", new Location("l", "L", "luogo"));
    private final StoryProgress progress = new StoryProgress();

    @Test
    void locationDetailIsVisibleOnlyWithEnoughAttribute() {
        Location location = new Location("bivio", "Bivio", "Un bivio.");
        location.addDetail(new ConditionalText(new AttributeCondition(Attribute.PERCEZIONE, 1), "Noti un graffio."));

        ConditionalText detail = location.getDetails().get(0);
        assertFalse(detail.isVisibleTo(player, progress));

        player.getStats().increase(Attribute.PERCEZIONE, 1);
        assertTrue(detail.isVisibleTo(player, progress));
    }

    @Test
    void mechanismHintIsOptionalAndConditional() {
        Mechanism plain = new Mechanism("a", "A", "d", new KeyUnlockCondition());
        assertTrue(plain.getHint().isEmpty());

        Mechanism hinted = new Mechanism("b", "B", "d", new KeyUnlockCondition())
                .withHint(new AttributeCondition(Attribute.SAPIENZA, 1), "Un indizio.");
        ConditionalText hint = hinted.getHint().orElseThrow();
        assertFalse(hint.isVisibleTo(player, progress));
        player.getStats().increase(Attribute.SAPIENZA, 1);
        assertTrue(hint.isVisibleTo(player, progress));
    }

    @Test
    void conditionalTextWithoutRequirementIsAlwaysVisible() {
        assertTrue(new ConditionalText(null, "sempre").isVisibleTo(player, progress));
    }

    @Test
    void blankTextIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new ConditionalText(null, " "));
    }

    @Test
    void locationRejectsNullDetail() {
        assertThrows(IllegalArgumentException.class, () -> new Location("l", "L", "d").addDetail(null));
    }
}