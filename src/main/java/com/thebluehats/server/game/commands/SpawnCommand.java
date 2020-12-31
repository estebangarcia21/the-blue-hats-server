package com.thebluehats.server.game.commands;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.CombatManager;
import com.thebluehats.server.game.managers.enchants.Timer;
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SpawnCommand extends GameCommand {
    private final CombatManager combatManager;
    private final Timer<Player> timer;
    private final RegionManager regionManager;

    @Inject
    public SpawnCommand(CombatManager combatManager, Timer<Player> timer, RegionManager regionManager) {
        this.combatManager = combatManager;
        this.timer = timer;
        this.regionManager = regionManager;
    }

    @Override
    public String[] getCommandNames() {
        return new String[] { "spawn" };
    }

    @Override
    public String getUsageMessage(String cmd) {
        return null;
    }

    @Override
    public void runCommand(Player player, String commandName, String[] args) {
        if (regionManager.entityIsInSpawn(player)) {
            player.sendMessage(ChatColor.RED + "You cannot /respawn here!");

            return;
        }

        if (!combatManager.playerIsInCombat(player)) {
            if (!timer.isRunning(player)) {
                player.setHealth(player.getMaxHealth());
                player.teleport(regionManager.getSpawnLocation(player));

                timer.start(player, 200);
            } else {
                player.sendMessage(ChatColor.RED + "You may only /respawn every 10 seconds");
            }
        } else {
            player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "HOLD UP! " + ChatColor.GRAY
                    + "Can't /respawn while fighting (" + ChatColor.RED + combatManager.getCombatTime(player) + "s"
                    + ChatColor.GRAY + " left)");
        }
    }
}