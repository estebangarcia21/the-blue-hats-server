package com.thebluehats.server.game.commands;

import com.thebluehats.server.game.utils.CommandUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdName = "duel";

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase(cmdName)) {
                if (args.length == 0) {
                    player.sendMessage(CommandUtils.formatMessage(cmdName, String.format("/%s <player>", cmdName)));
                } else {
                    if (Bukkit.getPlayer(args[0]) == null) {
                        player.sendMessage(CommandUtils.formatMessage("Error!", "This player is not online!"));

                        return true;
                    }

                    if (player == Bukkit.getPlayer(args[0])) {
                        player.sendMessage(CommandUtils.formatMessage("Error!", "You can not duel yourself!"));
                        return true;
                    }

                    // TODO Call duel manager
                }
            }
        }

        return true;
    }
}
