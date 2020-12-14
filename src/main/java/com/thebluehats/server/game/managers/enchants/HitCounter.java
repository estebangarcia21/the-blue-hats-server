package com.thebluehats.server.game.managers.enchants;

import java.util.HashMap;
import java.util.UUID;

import com.google.inject.Inject;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HitCounter {
    private final HashMap<UUID, HitCounterData> timerData = new HashMap<>();

    private final JavaPlugin plugin;

    @Inject
    public HitCounter(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void addOne(UUID playerUuid) {
        HitCounterData data = timerData.computeIfAbsent(playerUuid, k -> new HitCounterData());

        data.setHitResetTime(0L);
        data.setHitsWithEnchant(data.getHitsWithEnchant() + 1);

        startHitResetTimer(playerUuid);
    }

    public void add(UUID playerUuid, int amount) {
        HitCounterData data = timerData.computeIfAbsent(playerUuid, k -> new HitCounterData());

        data.setHitResetTime(0L);
        data.setHitsWithEnchant(data.getHitsWithEnchant() + amount);

        startHitResetTimer(playerUuid);
    }

    public boolean hasHits(Player player, int hitAmount) {
        HitCounterData data = timerData.computeIfAbsent(player.getUniqueId(), k -> new HitCounterData());

        if (data.getHitsWithEnchant() >= hitAmount) {
            data.setHitsWithEnchant(0);

            return true;
        }

        return false;
    }

    public void startHitResetTimer(UUID playerUuid) {
        HitCounterData data = timerData.computeIfAbsent(playerUuid, k -> new HitCounterData());

        data.setHitResetTaskId(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            data.setHitResetTime(data.getHitResetTime() - 1);

            if (data.getHitResetTime() <= 0) {
                data.setHitResetTime(0);

                Bukkit.getServer().getScheduler().cancelTask(data.getHitResetTaskId());
            }
        }, 0L, 20L));
    }
}

class HitCounterData {
    private int hitsWithEnchant;
    private long hitResetTime;
    private int hitResetTaskId;

    public HitCounterData() {
        this.hitsWithEnchant = 0;
        this.hitResetTime = 0;
        this.hitResetTaskId = 0;
    }

    public int getHitsWithEnchant() {
        return hitsWithEnchant;
    }

    public void setHitsWithEnchant(int value) {
        hitsWithEnchant = value;
    }

    public long getHitResetTime() {
        return hitResetTime;
    }

    public void setHitResetTime(long value) {
        hitResetTime = value;
    }

    public int getHitResetTaskId() {
        return hitResetTaskId;
    }

    public void setHitResetTaskId(int value) {
        hitResetTaskId = value;
    }
}
