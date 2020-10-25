package me.stevemmmmm.server.game.enchants;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.stevemmmmm.server.core.Main;

public class HitCounter {
    private Main mainInstance;
    private HashMap<UUID, HitCounterData> timerData;

    public HitCounter(Main mainInstance) {
        this.mainInstance = mainInstance;
    }

    public void updateHitCount(Player player) {
        HitCounterData data = timerData.get(player.getUniqueId());

        data.setHitResetTime(0L);
        data.setHitsWithEnchant(data.getHitsWithEnchant() + 1);

        startHitResetTimer(player);
    }

    public void updateHitCount(Player player, int amount) {
        HitCounterData data = timerData.get(player.getUniqueId());

        data.setHitResetTime(0L);
        data.setHitsWithEnchant(data.getHitsWithEnchant() + amount);

        startHitResetTimer(player);
    }

    public boolean hasRequiredHits(Player player, int hitAmount) {
        HitCounterData data = timerData.get(player.getUniqueId());

        if (data.getHitsWithEnchant() >= hitAmount) {
            data.setHitsWithEnchant(0);

            return true;
        }

        return false;
    }

    public void startHitResetTimer(Player player) {
        HitCounterData data = timerData.get(player.getUniqueId());

        data.setHitResetTaskId(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(mainInstance, () -> {
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
