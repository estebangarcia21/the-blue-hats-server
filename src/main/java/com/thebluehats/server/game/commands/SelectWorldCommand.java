package com.thebluehats.server.game.commands;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.game.WorldSelectionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectWorldCommand implements CommandExecutor {
    private final WorldSelectionManager manager;

    @Inject
    public SelectWorldCommand(WorldSelectionManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("selectworld")) {
               manager.displaySelectionMenu(player);
            }
        }

        return true;
    }
}