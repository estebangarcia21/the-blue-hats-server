package me.stevemmmmm.server.game.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("duel")) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.DARK_PURPLE + "Usage:" + ChatColor.RED + " /duel <player>");
                } else {
                    if (Bukkit.getPlayer(args[0]) == null) {
                        player.sendMessage(
                                ChatColor.DARK_PURPLE + "Error! " + ChatColor.RED + "This player is not online!");
                        return true;
                    }

                    if (player == Bukkit.getPlayer(args[0])) {
                        player.sendMessage(
                                ChatColor.DARK_PURPLE + "Error! " + ChatColor.RED + "You can not duel yourself!");
                        return true;
                    }

                    // TODO Dueling manager
//                    DuelingManager.getInstance().startDuel(new Duel(player, Bukkit.getPlayer(args[0])));
                }
            }
        }

        return true;
    }
}
