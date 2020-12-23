package com.thebluehats.server.game.commands;

import java.util.StringJoiner;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public abstract class GameCommand implements CommandExecutor {
    public void sendUsageMessage(Player player, String cmd, String description, String... args) {
        StringJoiner argsJoiner = new StringJoiner(" ");

        for (String arg : args) {
            argsJoiner.add("<" + arg + ">");
        }

        String usageMessage = ChatColor.DARK_PURPLE + "/" + cmd + " - " + ChatColor.RED + description
                + ChatColor.DARK_PURPLE + " | Usage" + ChatColor.DARK_PURPLE + "/" + cmd + " " + ChatColor.RED
                + argsJoiner.toString();

        player.sendMessage(usageMessage);
    }

    public void sendErrorMessage(Player player, String error) {
        player.sendMessage(ChatColor.DARK_PURPLE + "Error! " + ChatColor.RED + error);
    }
}
