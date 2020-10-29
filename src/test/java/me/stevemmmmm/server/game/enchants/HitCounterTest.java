package me.stevemmmmm.server.game.enchants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import me.stevemmmmm.server.core.Main;

public class HitCounterTest {
    @Mock
    private Player player;

    private HitCounter counter;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        counter = new HitCounter(mock(Main.class));
    }

    @Test(expected = NullPointerException.class)
    public void testAddOne() {
        counter.addOne(player);

        assertTrue(counter.hasHits(player, 1));
    }

    @Test(expected = NullPointerException.class)
    public void testAdd() {
        counter.add(player, 5);

        assertTrue(counter.hasHits(player, 5));
    }

    @Test
    public void testHasRequiredHits() {
        assertTrue(counter.hasHits(player, 0) && !counter.hasHits(player, 1));
    }

    @Test(expected = NullPointerException.class)
    public void testHitResetTimer() {
        counter.add(player, 5);

        assertTrue(counter.hasHits(player, 5));

        counter.startHitResetTimer(player);

        assertTrue(counter.hasHits(player, 0) && !counter.hasHits(player, 1));
    }
}
