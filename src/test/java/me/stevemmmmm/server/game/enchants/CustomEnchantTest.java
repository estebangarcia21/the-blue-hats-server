package me.stevemmmmm.server.game.enchants;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import me.stevemmmmm.server.game.managers.BowManager;
import me.stevemmmmm.server.game.utils.LoreBuilder;

public class CustomEnchantTest {
    @Mock
    private ItemStack item;
    @Mock
    private ItemMeta meta;

    private CustomEnchant enchant;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        enchant = new Wasp(new BowManager());

        when(item.getItemMeta()).thenReturn(meta);
        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp").build());
    }

    @Test
    public void testIsCompatibleWith() {
        CustomEnchant enchantMock = mock(CustomEnchant.class);

        when(enchantMock.getEnchantItemTypes()).thenReturn(new Material[] { Material.BOW });
        when(enchantMock.isCompatibleWith(Material.BOW)).thenCallRealMethod();

        assertTrue(enchantMock.isCompatibleWith(Material.BOW));
    }

    @Test
    public void testAttemptEnchantExecution() {
        assertTrue(enchant.canExecuteEnchant(item, null));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp II").build());
        assertTrue(enchant.canExecuteEnchant(item, null));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp III").build());
        assertTrue(enchant.canExecuteEnchant(item, null));
    }

    @Test
    public void testItemHasEnchant() {
        assertTrue(enchant.itemHasEnchant(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp II").build());
        assertTrue(enchant.itemHasEnchant(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp III").build());
        assertTrue(enchant.itemHasEnchant(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "").build());
        assertFalse(enchant.itemHasEnchant(item));
    }

    @Test
    public void testGetEnchantLevel() {
        assertEquals(1, enchant.getEnchantLevel(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp II").build());
        assertEquals(2, enchant.getEnchantLevel(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp III").build());
        assertEquals(3, enchant.getEnchantLevel(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "").build());
        assertEquals(0, enchant.getEnchantLevel(item));
    }
}
