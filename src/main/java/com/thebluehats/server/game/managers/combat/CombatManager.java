package com.thebluehats.server.game.managers.combat;

import java.util.HashMap;
import java.util.UUID;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.game.regionmanager.RegionManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CombatManager implements Listener {
    private final HashMap<UUID, CombatTimerData> data = new HashMap<>();

    private final JavaPlugin plugin;

    @Inject
    public CombatManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        data.put(event.getPlayer().getUniqueId(), new CombatTimerData());
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            combatTag((Player) event.getDamager());
            combatTag((Player) event.getEntity());
        }

        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                combatTag(((Player) ((Arrow) event.getDamager()).getShooter()));
                combatTag((Player) event.getEntity());
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        removePlayerFromCombat(event.getEntity());
    }

    public boolean playerIsInCombat(Player player) {
        return data.get(player.getUniqueId()).getTime() != 0;
    }

    public void combatTag(Player player) {
        CombatTimerData timerData = data.get(player.getUniqueId());
        if (RegionManager.getInstance().playerIsInRegion(player, RegionManager.RegionType.SPAWN))
            return;

        timerData.setTime(calculateCombatTime());

        if (!timerData.isOnCooldown()) {
            timerData.setTaskId(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (timerData.getTime() == 0) {
                    Bukkit.getServer().getScheduler().cancelTask(timerData.getTaskId());
                    return;
                }
                timerData.setTime(timerData.getTime() - 1);
            }, 0L, 20L));
        }
    }

    public int getCombatTime(Player player) {
        return data.get(player.getUniqueId()).getTime();
    }

    private int calculateCombatTime() {
        return 15;
    }

    private void removePlayerFromCombat(Player player) {
        CombatTimerData timerData = data.get(player.getUniqueId());

        if (playerIsInCombat(player)) {
            Bukkit.getServer().getScheduler().cancelTask(timerData.getTaskId());
            timerData.setTime(0);
        }
    }
}

class CombatTimerData {
    private int time;
    private int taskId;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public boolean isOnCooldown() {
        return time != 0;
    }
}