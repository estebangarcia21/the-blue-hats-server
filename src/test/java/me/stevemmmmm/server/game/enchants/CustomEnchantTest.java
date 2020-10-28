package me.stevemmmmm.server.game.enchants;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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

        assertEquals(true, enchantMock.isCompatibleWith(Material.BOW));
    }

    @Test
    public void testAttemptEnchantExecution() {
        assertEquals(true, enchant.canExecuteEnchant(item, null));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp II").build());
        assertEquals(true, enchant.canExecuteEnchant(item, null));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp III").build());
        assertEquals(true, enchant.canExecuteEnchant(item, null));
    }

    @Test
    public void testItemHasEnchant() {
        assertEquals(true, enchant.itemHasEnchant(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp II").build());
        assertEquals(true, enchant.itemHasEnchant(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp III").build());
        assertEquals(true, enchant.itemHasEnchant(item));

        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "").build());
        assertEquals(false, enchant.itemHasEnchant(item));
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

    @Test
    public void testPlayerAndPlayer() {
        Player damager = mock(Player.class);
        Player damagee = mock(Player.class);

        PlayerInventory inventory = mock(PlayerInventory.class);
        World world = mock(World.class);

        when(damagee.getInventory()).thenReturn(inventory);
        when(damagee.getWorld()).thenReturn(world);
        when(damager.getWorld()).thenReturn(world);

        when(world.getName()).thenReturn("");

        when(inventory.getItemInMainHand()).thenReturn(item);

        assertEquals(true, enchant.playerAndPlayer(damager, damagee, inv -> inv.getItemInMainHand(), level -> {
        }));
    }

    @Test
    public void testArrowAndPlayer() {
        Arrow arrow = mock(Arrow.class);
        Player damager = mock(Player.class);
        Player damagee = mock(Player.class);

        PlayerInventory inventory = mock(PlayerInventory.class);
        World world = mock(World.class);

        when(arrow.getShooter()).thenReturn(damager);
        when(damagee.getInventory()).thenReturn(inventory);
        when(damagee.getWorld()).thenReturn(world);
        when(damager.getWorld()).thenReturn(world);

        when(world.getName()).thenReturn("");

        when(inventory.getItemInMainHand()).thenReturn(item);

        assertEquals(true, enchant.arrowAndPlayer(arrow, damagee, inv -> inv.getItemInMainHand(), level -> {
        }));
    }
}
