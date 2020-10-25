package me.stevemmmmm.server.game.managers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.junit.Test;

public class DamageManagerTest {
    @Test
    public void testPlayerIsInCanceledEvent() {
        DamageManager manager = new DamageManager();
        Player arrow = mock(Player.class);

        assertEquals(false, manager.playerIsInCanceledEvent(arrow));
    }

    @Test
    public void testArrowIsInCanceledEvent() {
        DamageManager manager = new DamageManager();
        Arrow arrow = mock(Arrow.class);

        assertEquals(false, manager.arrowIsInCanceledEvent(arrow));
    }
}
