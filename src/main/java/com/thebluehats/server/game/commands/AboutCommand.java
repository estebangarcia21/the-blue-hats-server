package com.thebluehats.server.game.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AboutCommand extends GameCommand {
    @Override
    public String[] getCommandNames() {
        return new String[] { "about" };
    }

    @Override
    public String getUsageMessage(String cmd) {
        return null;
    }

    @Override
    public void runCommand(Player player, String commandName, String[] args) {
        String pluginName = "The Blue Hats Server";
        String version = "v1.0";
        String creatorDiscord = "Stevemmmmm#8894";

        player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + pluginName + " " + version);
        player.sendMessage(ChatColor.YELLOW + "by " + ChatColor.RED + "Stevemmmmm");

        for (int i = 0; i < 7; i++)
            player.sendMessage(" ");

        player.sendMessage(ChatColor.DARK_PURPLE.toString() + ChatColor.UNDERLINE + "Discord" + ChatColor.YELLOW + " ▶ "
                + ChatColor.BLUE + creatorDiscord);
    }
}