package me.stevemmmmm.server.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.stevemmmmm.server.core.Main;
import me.stevemmmmm.server.game.managers.RegionManager;

public class SpawnCommand implements CommandExecutor {
    private final HashMap<UUID, Integer> cooldownTasks = new HashMap<>();
    private final HashMap<UUID, Integer> cooldownTime = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("spawn") || label.equalsIgnoreCase("respawn")) {
                if (RegionManager.getInstance().playerIsInRegion(player, RegionManager.RegionType.SPAWN)) {
                    player.sendMessage(ChatColor.RED + "You cannot /respawn here!");
                    return true;
                }

                if (!CombatManager.getInstance().playerIsInCombat(player)) {
                    if (!cooldownTasks.containsKey(player.getUniqueId())) {
                        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                        player.teleport(RegionManager.getInstance().getSpawnLocation(player));

                        cooldownTime.put(player.getUniqueId(), 10);

                        cooldownTasks.put(player.getUniqueId(),
                                Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.INSTANCE, () -> {
                                    cooldownTime.put(player.getUniqueId(), cooldownTime.get(player.getUniqueId()) - 1);

                                    if (cooldownTime.get(player.getUniqueId()) <= 0f) {
                                        cooldownTime.remove(player.getUniqueId());
                                        Bukkit.getServer().getScheduler()
                                                .cancelTask(cooldownTasks.get(player.getUniqueId()));
                                        cooldownTasks.remove(player.getUniqueId());
                                    }
                                }, 0L, 20L));
                    } else {
                        player.sendMessage(ChatColor.RED + "You may only /respawn every 10 seconds");
                    }
                } else {
                    player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "HOLD UP! " + ChatColor.GRAY
                            + "Can't /respawn while fighting (" + ChatColor.RED
                            + CombatManager.getInstance().getCombatTime(player) + "s" + ChatColor.GRAY + " left)");
                }
            }
        }

        return true;
    }
}