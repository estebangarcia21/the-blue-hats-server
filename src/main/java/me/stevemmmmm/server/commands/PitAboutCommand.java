package me.stevemmmmm.server.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.stevemmmmm.server.core.Main;

public class PitAboutCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("pitabout")) {
                if (args.length == 0) {
                    player.sendMessage(
                            ChatColor.YELLOW.toString() + ChatColor.BOLD + "The Hypixel Pit Remake " + Main.version);
                    player.sendMessage(ChatColor.YELLOW + "by " + ChatColor.RED + "Stevemmmmm");
                    player.sendMessage(" ");
                    player.sendMessage(" ");
                    player.sendMessage(" ");
                    player.sendMessage(" ");
                    player.sendMessage(" ");
                    player.sendMessage(" ");
                    player.sendMessage(" ");
                    player.sendMessage(ChatColor.DARK_PURPLE.toString() + ChatColor.UNDERLINE + "Discord"
                            + ChatColor.YELLOW + " ▶ " + ChatColor.BLUE + "Stevemmmmm#9796");
                }
            }
        }

        return true;
    }
}