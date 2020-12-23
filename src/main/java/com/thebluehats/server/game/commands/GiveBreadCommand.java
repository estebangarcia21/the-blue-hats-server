package com.thebluehats.server.game.commands;

import java.util.ArrayList;

import com.thebluehats.server.game.utils.LoreParser;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveBreadCommand extends GameCommand {
    @Override
    public String[] getCommandNames() {
        return new String[] { "givebread" };
    }

    @Override
    public String getUsageMessage(String cmd) {
        return formatStandardUsageMessage(cmd, "Gives a stack of Yummy Bread.");
    }

    @Override
    public void runCommand(Player player, String commandName, String[] args) {
        ItemStack bread = new ItemStack(Material.BREAD, 64);
        ItemMeta meta = bread.getItemMeta();

        ArrayList<String> breadLore = new LoreParser("Heals <red>4❤</red></br>Grants 1❤").parse();

        meta.setDisplayName(ChatColor.GOLD + "Yummy Bread");
        meta.setLore(breadLore);

        bread.setItemMeta(meta);

        player.getInventory().addItem(bread);
    }
}
