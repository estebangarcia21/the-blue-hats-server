package me.stevemmmmm.server.game.commands;

import me.stevemmmmm.server.game.managers.WorldSelectionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectWorldCommand implements CommandExecutor {
    private WorldSelectionManager manager;

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