package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.utils.LoreBuilder;
import com.thebluehats.server.game.managers.combat.BowManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomEnchantTest {
    @Mock
    private CustomEnchant mockEnchant;
    @Mock
    private ItemStack item;
    @Mock
    private ItemMeta meta;

    private CustomEnchant realEnchant;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        realEnchant = new Wasp(new BowManager());

        when(item.getItemMeta()).thenReturn(meta);
        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, realEnchant.getName()).build());

        mockEnchant = mock(CustomEnchant.class);

        when(mockEnchant.isCompatibleWith(any())).thenCallRealMethod();
    }

    @Test
    public void IsCompatibleWhenMaterialsAreEqual() {
        when(mockEnchant.getEnchantItemTypes()).thenReturn(new Material[] { Material.BOW });

        assertTrue(mockEnchant.isCompatibleWith(Material.BOW));
    }

    @Test
    public void IsNotCompatibleWhenMaterialsAreNotEqual() {
        when(mockEnchant.getEnchantItemTypes()).thenReturn(new Material[] { Material.ARROW });

        assertFalse(mockEnchant.isCompatibleWith(Material.COBBLESTONE));
    }

    @Test
    public void ExecutesEnchantWhenItemHasProperLore() {
        assertTrue(realEnchant.canExecuteEnchant(item, null));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, realEnchant.getName() + " II").build());
        assertTrue(realEnchant.canExecuteEnchant(item, null));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, realEnchant.getName() + " III").build());
        assertTrue(realEnchant.canExecuteEnchant(item, null));
    }

    @Test
    public void DoesNotExecuteEnchantWhenItemHasImproperLore() {
        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, realEnchant.getName() + " IV").build());
        assertFalse(realEnchant.itemHasEnchant(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "").build());
        assertFalse(realEnchant.itemHasEnchant(item));
    }

    @Test
    public void GetsEnchantLevelWhenItemHasProperLore() {
        assertEquals(1, realEnchant.getEnchantLevel(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, realEnchant.getName() + " II").build());
        assertEquals(2, realEnchant.getEnchantLevel(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, realEnchant.getName() + " III").build());
        assertEquals(3, realEnchant.getEnchantLevel(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "").build());
        assertEquals(0, realEnchant.getEnchantLevel(item));
    }
}
