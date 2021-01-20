package com.thebluehats.server.game.managers.enchants;

import java.util.HashMap;
import java.util.UUID;

import com.google.inject.Inject;

import org.bukkit.entity.Player;

public class HitCounter {
    private final HashMap<UUID, HitCounterData> timerData = new HashMap<>();

    private final Timer<UUID> timer;

    @Inject
    public HitCounter(Timer<UUID> timer) {
        this.timer = timer;
    }

    public void addOne(Player player) {
        HitCounterData data = timerData.computeIfAbsent(player.getUniqueId(), k -> new HitCounterData());

        data.setHitResetTime(0L);
        data.setHitsWithEnchant(data.getHitsWithEnchant() + 1);

        startHitResetTimer(player);
    }

    public void add(Player player, int amount) {
        HitCounterData data = timerData.computeIfAbsent(player.getUniqueId(), k -> new HitCounterData());

        data.setHitResetTime(0L);
        data.setHitsWithEnchant(data.getHitsWithEnchant() + amount);

        startHitResetTimer(player);
    }

    public boolean hasHits(Player player, int hitAmount) {
        HitCounterData data = timerData.computeIfAbsent(player.getUniqueId(), k -> new HitCounterData());

        if (data.getHitsWithEnchant() >= hitAmount) {
            data.setHitsWithEnchant(0);

            return true;
        }

        return false;
    }

    public void startHitResetTimer(Player player) {
        HitCounterData data = timerData.computeIfAbsent(player.getUniqueId(), k -> new HitCounterData());

        timer.start(player.getUniqueId(), 3 * 20, true, () -> data.setHitsWithEnchant(0));
    }

    private static class HitCounterData {
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
}