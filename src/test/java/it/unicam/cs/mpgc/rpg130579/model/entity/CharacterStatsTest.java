package it.unicam.cs.mpgc.rpg130579.model.entity;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CharacterStatsTest {
    private final CharacterStats stats = new CharacterStats();

    @Test
    void attributesStartAtZero() {
        for (Attribute a : Attribute.values()) assertEquals(0, stats.get(a));
    }

    @Test
    void increaseAccumulates() {
        stats.increase(Attribute.SAPIENZA, 1);
        stats.increase(Attribute.SAPIENZA, 2);
        assertEquals(3, stats.get(Attribute.SAPIENZA));
        assertEquals(0, stats.get(Attribute.PERSUASIONE));
    }

    @Test
    void invalidArgumentsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> stats.increase(null, 1));
        assertThrows(IllegalArgumentException.class, () -> stats.increase(Attribute.SAPIENZA, 0));
        assertThrows(IllegalArgumentException.class, () -> stats.increase(Attribute.SAPIENZA, -1));
    }

    @Test
    void restoreReplacesEverything() {
        stats.increase(Attribute.PERCEZIONE, 5);
        stats.restore(Map.of(Attribute.SAPIENZA, 2));
        assertEquals(2, stats.get(Attribute.SAPIENZA));
        assertEquals(0, stats.get(Attribute.PERCEZIONE));
    }

    @Test
    void restoreRejectsNegativeValues() {
        assertThrows(IllegalArgumentException.class, () -> stats.restore(Map.of(Attribute.SAPIENZA, -1)));
    }

    @Test
    void asMapIsACopy() {
        stats.increase(Attribute.SAPIENZA, 1);
        Map<Attribute, Integer> copy = stats.asMap();
        stats.increase(Attribute.SAPIENZA, 1);
        assertEquals(1, copy.get(Attribute.SAPIENZA));
    }
}
