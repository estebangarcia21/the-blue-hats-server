package me.stevemmmmm.server.game.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.stevemmmmm.server.game.utils.LoreBuilder;

public class GiveBreadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("givebread")) {
                ItemStack bread = new ItemStack(Material.BREAD, 64);
                ItemMeta meta = bread.getItemMeta();

                meta.setDisplayName(ChatColor.GOLD + "Yummy Bread");
                meta.setLore(new LoreBuilder().write("Heals ").write(ChatColor.RED, "4❤").next().write("Grants ")
                        .write(ChatColor.GOLD, "1❤").build());

                bread.setItemMeta(meta);

                player.getInventory().addItem(bread);
            }
        }

        return true;
    }
}
