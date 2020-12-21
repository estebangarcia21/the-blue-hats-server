package com.thebluehats.server.game.commands;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.CombatManager;
import com.thebluehats.server.game.managers.enchants.CooldownTimer;
import com.thebluehats.server.game.managers.game.regionmanager.RegionManager;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    private final CombatManager manager;
    private final CooldownTimer cooldownTimer;
    private final RegionManager regionManager;

    @Inject
    public SpawnCommand(CombatManager manager, CooldownTimer cooldownTimer, RegionManager regionManager) {
        this.manager = manager;
        this.cooldownTimer = cooldownTimer;
        this.regionManager = regionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("spawn") || label.equalsIgnoreCase("respawn")) {
                if (regionManager.playerIsInRegion(player, RegionManager.RegionType.SPAWN)) {
                    player.sendMessage(ChatColor.RED + "You cannot /respawn here!");
                    return true;
                }

                if (!manager.playerIsInCombat(player)) {
                    if (!cooldownTimer.isOnCooldown(player)) {
                        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                        player.teleport(regionManager.getSpawnLocation(player));

                        cooldownTimer.startCooldown(player, 200);
                    } else {
                        player.sendMessage(ChatColor.RED + "You may only /respawn every 10 seconds");
                    }
                } else {
                    player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "HOLD UP! " + ChatColor.GRAY
                            + "Can't /respawn while fighting (" + ChatColor.RED + manager.getCombatTime(player) + "s"
                            + ChatColor.GRAY + " left)");
                }
            }
        }

        return true;
    }
}