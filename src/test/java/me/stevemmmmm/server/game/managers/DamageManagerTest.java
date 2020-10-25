package me.stevemmmmm.server.game.managers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.junit.Test;

public class DamageManagerTest {
    @Test
    public void testPlayerIsInCanceledEvent() {
        DamageManager manager = new DamageManager();
        ArrayList<UUID> canceledPlayers = manager.getCanceledPlayers();

        Player player = mock(Player.class);
        canceledPlayers.add(player.getUniqueId());

        assertEquals(true, manager.playerIsInCanceledEvent(player));
    }

    @Test
    public void testArrowIsInCanceledEvent() {
        DamageManager manager = new DamageManager();
        ArrayList<UUID> canceledPlayers = manager.getCanceledPlayers();

        Arrow arrow = mock(Arrow.class);
        Player player = mock(Player.class);

        when(arrow.getShooter()).thenReturn(player);

        canceledPlayers.add(player.getUniqueId());

        assertEquals(true, manager.arrowIsInCanceledEvent(arrow));
    }
}
