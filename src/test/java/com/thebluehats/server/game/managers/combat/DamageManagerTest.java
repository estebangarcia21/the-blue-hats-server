package com.thebluehats.server.game.managers.combat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

import com.thebluehats.server.core.Main;
import com.thebluehats.server.game.enchants.Mirror;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;

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
        Main main = mock(Main.class);

        manager = new DamageManager(new Mirror(null), new CustomEnchantManager(main), new CombatManager(main));

        try {
            Field field;
            field = manager.getClass().getDeclaredField("canceledPlayers");

            field.setAccessible(true);

            Object value = field.get(manager);

            if (value instanceof ArrayList<?>) {
                @SuppressWarnings("unchecked")
                ArrayList<UUID> canceledPlayers = (ArrayList<UUID>) value;

                this.canceledPlayers = canceledPlayers;
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void PlayerIsInCanceledEventWhenInCanceledEventsList() {
        canceledPlayers.add(player.getUniqueId());
        assertTrue(manager.playerIsInCanceledEvent(player));

        canceledPlayers.remove(player.getUniqueId());
        assertFalse(manager.playerIsInCanceledEvent(player));
    }

    @Test
    public void ArrowIsInCanceledEventWhenShooterIsInCanceledEventsList() {
        Arrow arrow = mock(Arrow.class);

        when(arrow.getShooter()).thenReturn(player);

        canceledPlayers.add(player.getUniqueId());
        assertTrue(manager.arrowIsInCanceledEvent(arrow));

        canceledPlayers.remove(player.getUniqueId());
        assertFalse(manager.arrowIsInCanceledEvent(arrow));
    }
}
