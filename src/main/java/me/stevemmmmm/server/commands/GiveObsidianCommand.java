package me.stevemmmmm.server.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveObsidianCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("giveobsidian")) {
                ItemStack obsidian = new ItemStack(Material.OBSIDIAN, 64);

                player.getInventory().addItem(obsidian);
            }
        }

        return true;
    }
}