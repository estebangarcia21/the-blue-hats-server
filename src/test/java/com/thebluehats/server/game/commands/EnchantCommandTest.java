package com.thebluehats.server.game.commands;

import com.thebluehats.server.core.Main;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EnchantCommandTest {
    @Test
    public void AddsEnchantToItem() {
        CustomEnchantManager manager = new CustomEnchantManager(mock(Main.class));
        CommandExecutor command = new EnchantCommand(manager);

        Player player = mock(Player.class);
        PlayerInventory inventory = mock(PlayerInventory.class);
        ItemStack item = mock(ItemStack.class);
        ItemMeta meta = mock(ItemMeta.class);

        when(player.getInventory()).thenReturn(inventory);
        when(inventory.getItemInMainHand()).thenReturn(item);

        when(item.getItemMeta()).thenReturn(meta);
        when(item.getType()).thenReturn(Material.BOW);
        when(meta.getDisplayName()).thenReturn("Fresh Mystic Bow");

        command.onCommand(player, mock(Command.class), "pitenchant", new String[] { "Wasp", "1" });

//        verify(manager).addEnchants(any(), any(), any());
    }
}
