package com.thebluehats.server.game.managers.enchants;

import java.util.HashMap;
import java.util.UUID;

import com.google.inject.Inject;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CooldownTimer {
    private final HashMap<UUID, CooldownTimerData> timerData = new HashMap<>();

    private final JavaPlugin plugin;

    @Inject
    public CooldownTimer(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void startCooldown(Player player, long ticks) {
        CooldownTimerData data = timerData.computeIfAbsent(player.getUniqueId(), k -> new CooldownTimerData());

        data.setCooldownTime(ticks);

        data.setCooldownTaskId(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            data.setCooldownTime(data.getCooldownTime() - 1);

            if (data.getCooldownTime() <= 0) {
                data.setCooldownTime(0);

                Bukkit.getServer().getScheduler().cancelTask(data.getCooldownTaskId());
            }
        }, 0L, 1L));
    }

    public boolean isOnCooldown(Player player) {
        return timerData.computeIfAbsent(player.getUniqueId(), k -> new CooldownTimerData()).isOnCooldown();
    }

    private static class CooldownTimerData {
        private long cooldownTime;
        private int cooldownTaskId;

        public CooldownTimerData() {
            this.cooldownTime = 0;
            this.cooldownTaskId = 0;
        }

        public boolean isOnCooldown() {
            return getCooldownTime() != 0;
        }

        public long getCooldownTime() {
            return cooldownTime;
        }

        public void setCooldownTime(long value) {
            cooldownTime = value;
        }

        public int getCooldownTaskId() {
            return cooldownTaskId;
        }

        public void setCooldownTaskId(int value) {
            cooldownTaskId = value;
        }
    }
}