package com.thebluehats.server.game.commands;

import com.thebluehats.server.game.utils.WorkInProgress;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@WorkInProgress
public class DuelCommand extends GameCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdName = "duel";

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase(cmdName)) {
                if (args.length == 0) {
                    sendUsageMessage(player, cmdName, "Starts a duel with a player", "<level> ");
                } else {
                    Player inputPlayer = Bukkit.getPlayer(args[0]);

                    if (inputPlayer == null) {
                        sendErrorMessage(player, "This player is not online!");
                        return true;
                    } else if (inputPlayer == player) {
                        sendErrorMessage(player, "You can not duel yourself!");
                        return true;
                    }

                    // TODO Call duel start from the duel manager
                }
            }
        }

        return true;
    }
}
