package com.thebluehats.server.game.managers.combat;

import java.util.HashMap;
import java.util.UUID;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.game.regionmanager.RegionManager;
import com.thebluehats.server.game.utils.DataInitializer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CombatManager implements DataInitializer {
    private final HashMap<UUID, CombatTimerData> combatTimerData = new HashMap<>();

    private final JavaPlugin plugin;
    private final RegionManager regionManager;

    @Inject
    public CombatManager(JavaPlugin plugin, RegionManager regionManager) {
        this.plugin = plugin;
        this.regionManager = regionManager;
    }

    @Override
    @EventHandler
    public void initializeDataOnPlayerJoin(PlayerJoinEvent event) {
        combatTimerData.put(event.getPlayer().getUniqueId(), new CombatTimerData());
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            combatTag((Player) event.getDamager());
            combatTag((Player) event.getEntity());
        }

        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getEntity();
            Arrow arrow = (Arrow) event.getDamager();

            if (arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();

                combatTag(shooter);
                combatTag(damager);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        removePlayerFromCombat(event.getEntity());
    }

    public boolean playerIsInCombat(Player player) {
        return combatTimerData.get(player.getUniqueId()).isInCombat();
    }

    public void combatTag(Player player) {
        CombatTimerData timerData = combatTimerData.get(player.getUniqueId());

        if (regionManager.entityIsInSpawn(player))
            return;

        timerData.setTime(calculateCombatTime());

        if (!timerData.isInCombat()) {
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
        return combatTimerData.get(player.getUniqueId()).getTime();
    }

    private int calculateCombatTime() {
        /**
         * In reality, bounty time should be calulated here but it will be implemented
         * in the future when bounties are added to the project
         */
        return 15;
    }

    private void removePlayerFromCombat(Player player) {
        CombatTimerData timerData = combatTimerData.get(player.getUniqueId());

        if (playerIsInCombat(player)) {
            Bukkit.getServer().getScheduler().cancelTask(timerData.getTaskId());

            timerData.setTime(0);
        }
    }

    private class CombatTimerData {
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

        public boolean isInCombat() {
            return time != 0;
        }
    }
}