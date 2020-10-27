package me.stevemmmmm.server.game.enchants;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.bukkit.entity.Player;
import org.junit.Test;

import me.stevemmmmm.server.core.Main;

public class HitCounterTest {
    @Test(expected = NullPointerException.class)
    public void testAddOne() {
        Player player = mock(Player.class);
        HitCounter counter = new HitCounter(mock(Main.class));

        counter.addOne(player);

        assertEquals(true, counter.hasHits(player, 1));
    }

    @Test(expected = NullPointerException.class)
    public void testAdd() {
        Player player = mock(Player.class);
        HitCounter counter = new HitCounter(mock(Main.class));

        counter.add(player, 5);

        assertEquals(true, counter.hasHits(player, 5));
    }

    @Test
    public void testHasRequiredHits() {
        Player player = mock(Player.class);
        HitCounter counter = new HitCounter(mock(Main.class));

        assertEquals(true, counter.hasHits(player, 0) && !counter.hasHits(player, 1));
    }

    @Test(expected = NullPointerException.class)
    public void testHitResetTimer() {
        Player player = mock(Player.class);
        HitCounter counter = new HitCounter(mock(Main.class));

        counter.add(player, 5);

        assertEquals(true, counter.hasHits(player, 5));

        counter.startHitResetTimer(player);

        assertEquals(true, counter.hasHits(player, 0) && !counter.hasHits(player, 1));
    }
}
