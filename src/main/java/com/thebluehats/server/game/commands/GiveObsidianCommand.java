package com.thebluehats.server.game.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveObsidianCommand extends GameCommand {
    @Override
    public String[] getCommandNames() {
        return new String[] { "giveobsidian" };
    }

    @Override
    public String getUsageMessage(String cmd) {
        return formatStandardUsageMessage(cmd, "Gives a stack of obsidian.");
    }

    @Override
    public void runCommand(Player player, String commandName, String[] args) {
        ItemStack obsidian = new ItemStack(Material.OBSIDIAN, 64);

        player.getInventory().addItem(obsidian);
    }
}