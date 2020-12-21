package com.thebluehats.server.game.commands;

import java.util.ArrayList;

import com.thebluehats.server.game.utils.LoreParser;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveBreadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("givebread")) {
                ItemStack bread = new ItemStack(Material.BREAD, 64);
                ItemMeta meta = bread.getItemMeta();

                ArrayList<String> breadLore = new LoreParser("Heals <red>4❤</red></br>Grants 1❤").parse();

                meta.setDisplayName(ChatColor.GOLD + "Yummy Bread");
                meta.setLore(breadLore);

                bread.setItemMeta(meta);

                player.getInventory().addItem(bread);
            }
        }

        return true;
    }
}
