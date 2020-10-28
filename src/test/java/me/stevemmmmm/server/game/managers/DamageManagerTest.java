package me.stevemmmmm.server.game.managers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DamageManagerTest {
    @Mock
    private Player player;

    private DamageManager manager;
    private ArrayList<UUID> canceledPlayers;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        manager = new DamageManager();
        canceledPlayers = manager.getCanceledPlayers();
    }

    @Test
    public void testPlayerIsInCanceledEvent() {
        canceledPlayers.add(player.getUniqueId());
        assertEquals(true, manager.playerIsInCanceledEvent(player));

        canceledPlayers.remove(player.getUniqueId());
        assertEquals(false, manager.playerIsInCanceledEvent(player));
    }

    @Test
    public void testArrowIsInCanceledEvent() {
        Arrow arrow = mock(Arrow.class);

        when(arrow.getShooter()).thenReturn(player);

        canceledPlayers.add(player.getUniqueId());
        assertEquals(true, manager.arrowIsInCanceledEvent(arrow));

        canceledPlayers.remove(player.getUniqueId());
        assertEquals(false, manager.arrowIsInCanceledEvent(arrow));
    }
}
