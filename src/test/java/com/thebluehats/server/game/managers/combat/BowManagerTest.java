package com.thebluehats.server.game.managers.combat;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class BowManagerTest {
    @Mock
    private Arrow arrow;
    @Mock
    private Player player;

    private BowManager bowManager;

    private HashMap<Arrow, PlayerInventory> data;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        bowManager = new BowManager();

        Field dataField = bowManager.getClass().getDeclaredField("data");
        dataField.setAccessible(true);

        Object value = dataField.get(bowManager);

        if (value instanceof HashMap) {
            @SuppressWarnings("unchecked")
            HashMap<Arrow, PlayerInventory> data = (HashMap<Arrow, PlayerInventory>) dataField.get(bowManager);

            this.data = data;
        }
    }

    @Test
    public void AddsArrowOnArrowShootEvent() {
        when(arrow.getShooter()).thenReturn(player);

        bowManager.onArrowShoot(new EntityShootBowEvent(player, mock(ItemStack.class),
                mock(ItemStack.class), arrow, EquipmentSlot.HAND, 1f, false));

        assertTrue(data.containsKey(arrow));
    }

    @Test
    public void AddsArrowWhenRegistered() {
        bowManager.registerArrow(arrow, player);

        assertTrue(data.containsKey(arrow));
    }

    @Test(expected = NullPointerException.class)
    public void GetsBowFromArrow() {
        bowManager.registerArrow(arrow, player);

        bowManager.getBowFromArrow(arrow);
    }

    @Test(expected = NullPointerException.class)
    public void ReturnsPlayerLeggingsFromArrow() {
        bowManager.getLeggingsFromArrow(arrow);
    }
}
