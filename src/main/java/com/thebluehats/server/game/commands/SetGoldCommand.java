package com.thebluehats.server.game.commands;

import java.util.UUID;

import com.google.inject.Inject;
import com.thebluehats.server.api.models.PitDataModel;
import com.thebluehats.server.api.repos.Repository;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetGoldCommand implements CommandExecutor {
    private final Repository<UUID, PitDataModel> pitDataRepository;

    @Inject
    public SetGoldCommand(Repository<UUID, PitDataModel> pitDataRepository) {
        this.pitDataRepository = pitDataRepository;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("setgold")) {
                if (args.length > 0) {
                    if (args[0] != null) {
                        if (StringUtils.isNumeric(args[0])) {
                            double gold = Math.min(Double.parseDouble(args[0]), 1000000000);

                            pitDataRepository.update(player.getUniqueId(), model -> model.setGold(gold));
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
