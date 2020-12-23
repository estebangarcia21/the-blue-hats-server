package com.thebluehats.server.game.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveArrowCommand extends GameCommand {
    @Override
    public String[] getCommandNames() {
        return new String[] { "givearrows" };
    }

    @Override
    public String getUsageMessage(String cmd) {
        return formatStandardUsageMessage(cmd, "Gives a stack of arrows.");
    }

    @Override
    public void runCommand(Player player, String commandName, String[] args) {
        ItemStack arrows = new ItemStack(Material.ARROW, 64);

        player.getInventory().addItem(arrows);
    }
}
