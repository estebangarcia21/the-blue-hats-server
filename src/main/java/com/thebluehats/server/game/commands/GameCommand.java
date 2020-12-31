package com.thebluehats.server.game.commands;

import java.util.StringJoiner;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class GameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            for (String commandName : getCommandNames()) {
                if (label.equalsIgnoreCase(commandName)) {
                    String usageMessage = getUsageMessage(commandName);

                    if (usageMessage != null) {
                        if (args.length == 0 && usageMessage.contains("<") && usageMessage.contains(">")) {
                            player.sendMessage(usageMessage);
                        }
                    } else {
                        runCommand(player, commandName, args);
                    }
                }
            }
        } else {
            sender.sendMessage("Only players may run this command.");
        }

        return true;
    }

    public String formatStandardUsageMessage(String cmd, String description, String... args) {
        StringJoiner argsJoiner = new StringJoiner(" ");

        for (String arg : args) {
            argsJoiner.add("<" + arg + ">");
        }

        return ChatColor.DARK_PURPLE + "/" + cmd + " - " + ChatColor.RED + description + ChatColor.DARK_PURPLE
                + " | Usage" + ChatColor.DARK_PURPLE + "/" + cmd + " " + ChatColor.RED + argsJoiner.toString();
    }

    public String formatStandardErrorMessage(String error) {
        return ChatColor.DARK_PURPLE + "Error! " + ChatColor.RED + error;
    }

    public void registerCommand(JavaPlugin javaPlugin) {
        for (String commandName : getCommandNames()) {
            javaPlugin.getCommand(commandName).setExecutor(this);
        }
    }

    public abstract String[] getCommandNames();

    public abstract String getUsageMessage(String cmd);

    public abstract void runCommand(Player player, String cmd, String[] args);
}
