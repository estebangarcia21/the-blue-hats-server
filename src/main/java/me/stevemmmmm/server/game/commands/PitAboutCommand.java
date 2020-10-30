package me.stevemmmmm.server.game.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.stevemmmmm.server.core.Main;

public class PitAboutCommand implements CommandExecutor {
    private Main main;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("pitabout")) {
                if (args.length == 0) {
                    player.sendMessage(
                            ChatColor.YELLOW.toString() + ChatColor.BOLD + main.getPluginName() + " " + main.getVersion());
                    player.sendMessage(ChatColor.YELLOW + "by " + ChatColor.RED + "Stevemmmmm");

                    for (int i = 0; i < 7; i++)  player.sendMessage(" ");

                    player.sendMessage(ChatColor.DARK_PURPLE.toString() + ChatColor.UNDERLINE + "Discord"
                            + ChatColor.YELLOW + " ▶ " + ChatColor.BLUE + main.getDiscord());
                }
            }
        }

        return true;
    }
}