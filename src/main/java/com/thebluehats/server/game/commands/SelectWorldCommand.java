package com.thebluehats.server.game.commands;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.world.WorldSelectionManager;

import org.bukkit.entity.Player;

public class SelectWorldCommand extends GameCommand {
    private final WorldSelectionManager worldSelectionManager;

    @Inject
    public SelectWorldCommand(WorldSelectionManager manager) {
        this.worldSelectionManager = manager;
    }

    @Override
    public String[] getCommandNames() {
        return new String[] { "selectworld" };
    }

    @Override
    public String getUsageMessage(String cmd) {
        return formatStandardUsageMessage(cmd, "Displays the world selection menu.");
    }

    @Override
    public void runCommand(Player player, String commandName, String[] args) {
        worldSelectionManager.displaySelectionMenu(player);
    }
}