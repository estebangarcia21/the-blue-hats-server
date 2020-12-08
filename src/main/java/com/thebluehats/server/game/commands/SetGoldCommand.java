package com.thebluehats.server.game.commands;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.game.GrindingSystem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetGoldCommand implements CommandExecutor {
    private final GrindingSystem system;

    @Inject
    public SetGoldCommand(GrindingSystem system) {
        this.system = system;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("setgold")) {
                if (args.length > 0) {
                    if (args[0] != null) {
                        if (StringUtils.isNumeric(args[0])) {
                            double gold = Double.parseDouble(args[0]);

                            if (gold > 1000000000.00)
                                gold = 1000000000.00;

                            // TODO Implement grinding system
                            // system.setPlayerGold(player, gold);
                        } else {
                            player.sendMessage(
                                    ChatColor.RED + "Error! " + ChatColor.DARK_PURPLE + "Usage: /setgold <amount>");
                        }
                    } else {
                        player.sendMessage(
                                ChatColor.RED + "Error! " + ChatColor.DARK_PURPLE + "Usage: /setgold <amount>");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Error! " + ChatColor.DARK_PURPLE + "Usage: /setgold <amount>");
                }
            }
        }

        return true;
    }
}
