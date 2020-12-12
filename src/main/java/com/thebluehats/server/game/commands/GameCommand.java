package com.thebluehats.server.game.commands;

import java.util.StringJoiner;

import com.thebluehats.server.game.utils.WorkInProgress;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public abstract class GameCommand implements CommandExecutor {
    private final boolean isWorkInProgress;

    public GameCommand() {
        this.isWorkInProgress = getClass().isAnnotationPresent(WorkInProgress.class);
    }

    public void sendUsageMessage(Player player, String cmd, String description, String... args) {
        StringJoiner argsJoiner = new StringJoiner(" ");

        for (String arg : args) {
            argsJoiner.add("<" + arg + ">");
        }

        if (isWorkInProgress) {
            player.sendMessage(ChatColor.BOLD.toString() + ChatColor.RED + "WARNING!" + ChatColor.YELLOW
                    + " This command is a WORK IN PROGRESS! It may or may not work as intended.");
        }

        player.sendMessage(String.format("/%1$s - %1$s | Usage: /%1$s %2$s", ChatColor.DARK_PURPLE + cmd,
                ChatColor.RED + description, argsJoiner.toString()));
    }

    public String sendErrorMessage(Player player, String error) {
        return String.format("%sError! %s", ChatColor.DARK_PURPLE, ChatColor.RED + error);
    }
}
