package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.managers.combat.BowManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.EventTemplate;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.utils.LoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomEnchantTest {
    @Test
    public void IsCompatibleWhenMaterialsAreEqual() {
        CustomEnchant<?> realEnchant = new Wasp(new BowManager(), new EventTemplate[] { new ArrowHitPlayerTemplate() });
        CustomEnchant<?> mockEnchant = mock(CustomEnchant.class);
        ItemStack mockItem = mock(ItemStack.class);
        ItemMeta mockItemMeta = mock(ItemMeta.class);

        when(mockItem.getItemMeta()).thenReturn(mockItemMeta);
        when(mockItemMeta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, realEnchant.getName()).build());

        when(mockEnchant.isCompatibleWith(any())).thenCallRealMethod();
        when(mockEnchant.getEnchantItemTypes()).thenReturn(new Material[] { Material.BOW });

        assertTrue(mockEnchant.isCompatibleWith(Material.BOW));
    }

    @Test
    public void IsNotCompatibleWhenMaterialsAreNotEqual() {
        CustomEnchant<?> realEnchant = new Wasp(new BowManager(), new EventTemplate[] { new ArrowHitPlayerTemplate() });
        CustomEnchant<?> mockEnchant = mock(CustomEnchant.class);
        ItemStack mockItem = mock(ItemStack.class);
        ItemMeta mockItemMeta = mock(ItemMeta.class);

        when(mockItem.getItemMeta()).thenReturn(mockItemMeta);
        when(mockItemMeta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, realEnchant.getName()).build());

        when(mockEnchant.isCompatibleWith(any())).thenCallRealMethod();
        when(mockEnchant.getEnchantItemTypes()).thenReturn(new Material[] { Material.BOW });

        when(mockEnchant.getEnchantItemTypes()).thenReturn(new Material[] { Material.ARROW });

        assertFalse(mockEnchant.isCompatibleWith(Material.COBBLESTONE));
    }

    @Test
    public void ExecutesEnchantWhenItemHasProperLore() {
        CustomEnchant<?> realEnchant = new Wasp(new BowManager(), new EventTemplate[] { new ArrowHitPlayerTemplate() });
        CustomEnchant<?> mockEnchant = mock(CustomEnchant.class);
        ItemStack mockItem = mock(ItemStack.class);
        ItemMeta mockItemMeta = mock(ItemMeta.class);

        when(mockItem.getItemMeta()).thenReturn(mockItemMeta);
        when(mockEnchant.isCompatibleWith(any())).thenCallRealMethod();
        when(mockEnchant.getEnchantItemTypes()).thenReturn(new Material[] { Material.BOW });

        when(mockItemMeta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, realEnchant.getName()).build());
        assertTrue(realEnchant.canExecuteEnchant(mockItem, null));

        when(mockItemMeta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, realEnchant.getName() + " II").build());
        assertTrue(realEnchant.canExecuteEnchant(mockItem, null));

        when(mockItemMeta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, realEnchant.getName() + " III").build());
        assertTrue(realEnchant.canExecuteEnchant(mockItem, null));
    }

    @Test
    public void DoesNotExecuteEnchantWhenItemHasImproperLore() {
        CustomEnchant<?> realEnchant = new Wasp(new BowManager(), new EventTemplate[] { new ArrowHitPlayerTemplate() });
        CustomEnchant<?> mockEnchant = mock(CustomEnchant.class);
        ItemStack mockItem = mock(ItemStack.class);
        ItemMeta mockItemMeta = mock(ItemMeta.class);

        when(mockItem.getItemMeta()).thenReturn(mockItemMeta);
        when(mockItemMeta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, realEnchant.getName()).build());

        when(mockEnchant.isCompatibleWith(any())).thenCallRealMethod();
        when(mockEnchant.getEnchantItemTypes()).thenReturn(new Material[] { Material.BOW });

        when(mockItemMeta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, realEnchant.getName() + " IV").build());
        assertFalse(realEnchant.itemHasEnchant(mockItem));

        when(mockItemMeta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "").build());
        assertFalse(realEnchant.itemHasEnchant(mockItem));
    }

    @Test
    public void GetsEnchantLevelWhenItemHasProperLore() {
        CustomEnchant<?> realEnchant = new Wasp(new BowManager(), new EventTemplate[] { new ArrowHitPlayerTemplate() });
        CustomEnchant<?> mockEnchant = mock(CustomEnchant.class);
        ItemStack mockItem = mock(ItemStack.class);
        ItemMeta mockItemMeta = mock(ItemMeta.class);

        when(mockItem.getItemMeta()).thenReturn(mockItemMeta);
        when(mockEnchant.isCompatibleWith(any())).thenCallRealMethod();
        when(mockEnchant.getEnchantItemTypes()).thenReturn(new Material[] { Material.BOW });

        when(mockItemMeta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, realEnchant.getName()).build());
        assertEquals(1, realEnchant.getEnchantLevel(mockItem));

        when(mockItemMeta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, realEnchant.getName() + " II").build());
        assertEquals(2, realEnchant.getEnchantLevel(mockItem));

        when(mockItemMeta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, realEnchant.getName() + " III").build());
        assertEquals(3, realEnchant.getEnchantLevel(mockItem));

        when(mockItemMeta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "").build());
        assertEquals(0, realEnchant.getEnchantLevel(mockItem));
    }
}
