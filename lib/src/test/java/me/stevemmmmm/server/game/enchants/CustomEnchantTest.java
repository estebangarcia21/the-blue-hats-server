package me.stevemmmmm.server.game.enchants;

import static org.junit.Assert.assertEquals;
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

    private Wasp enchant;

    @Before
    public void setUp() {
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

        assertEquals(true, enchantMock.isCompatibleWith(Material.BOW));
    }

    @Test
    public void testAttemptEnchantExecution() {
        assertEquals(true, enchant.attemptEnchantExecution(item, null));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp II").build());
        assertEquals(true, enchant.attemptEnchantExecution(item, null));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp III").build());
        assertEquals(true, enchant.attemptEnchantExecution(item, null));
    }

    @Test
    public void testItemHasEnchant() {
        assertEquals(true, enchant.itemHasEnchant(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp II").build());
        assertEquals(true, enchant.itemHasEnchant(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp III").build());
        assertEquals(true, enchant.itemHasEnchant(item));
    }

    @Test
    public void testGetEnchantLevel() {
        assertEquals(1, enchant.getEnchantLevel(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp II").build());
        assertEquals(2, enchant.getEnchantLevel(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp III").build());
        assertEquals(3, enchant.getEnchantLevel(item));
    }
}
