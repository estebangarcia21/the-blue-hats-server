package com.thebluehats.server.game.commands;

import java.util.StringJoiner;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class GameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String commandName = getCommandName();

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase(commandName)) {
                if (args.length == 0) {
                    player.sendMessage(getUsageMessage(commandName));

                    return true;
                }
            } else {
                runCommand(player, args);
            }
        } else {
            sender.sendMessage("Only players may run this command.");
        }

        return true;
    }

    public String formatUsageMessage(String cmd, String description, String... args) {
        StringJoiner argsJoiner = new StringJoiner(" ");

        for (String arg : args) {
            argsJoiner.add("<" + arg + ">");
        }

        return ChatColor.DARK_PURPLE + "/" + cmd + " - " + ChatColor.RED + description + ChatColor.DARK_PURPLE
                + " | Usage" + ChatColor.DARK_PURPLE + "/" + cmd + " " + ChatColor.RED + argsJoiner.toString();
    }

    public String formatErrorMessage(String error) {
        return ChatColor.DARK_PURPLE + "Error! " + ChatColor.RED + error;
    }

    public abstract String getCommandName();

    public abstract String getUsageMessage(String cmd);

    public abstract void runCommand(Player player, String[] args);
}
