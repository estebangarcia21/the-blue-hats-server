package com.thebluehats.server.game.managers.enchants;

import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Set;

public class Timer<K> {
    private final HashMap<K, TimerData> timerData = new HashMap<>();

    private final JavaPlugin plugin;

    @Inject
    public Timer(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start(K key, long ticks) {
        TimerData data = timerData.computeIfAbsent(key, k -> new TimerData());

        data.setTime(ticks);

        data.setTaskId(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            data.setTime(data.getTime() - 1);

            if (data.getTime() <= 0) {
                data.setTime(0);

                Bukkit.getServer().getScheduler().cancelTask(data.getTaskId());
                timerData.remove(key);
            }
        }, 0L, 1L));
    }

    public void start(K key, long ticks, Runnable post) {
        TimerData data = timerData.computeIfAbsent(key, k -> new TimerData());

        data.setTime(ticks);

        data.setTaskId(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            data.setTime(data.getTime() - 1);

            if (data.getTime() <= 0) {
                data.setTime(0);

                Bukkit.getServer().getScheduler().cancelTask(data.getTaskId());
                timerData.remove(key);

                post.run();
            }
        }, 0L, 1L));
    }

    public void cancel(K key) {
        TimerData data = timerData.computeIfAbsent(key, k -> new TimerData());

        Bukkit.getServer().getScheduler().cancelTask(data.getTaskId());
        timerData.remove(key);
    }

    public Set<K> getKeys() {
        return timerData.keySet();
    }

    public boolean isRunning(K key) {
        TimerData data = timerData.get(key);

        return data != null && data.isRunning();
    }

    public long getTime(K key) {
        TimerData data = timerData.get(key);

        return data != null ? data.getTime() : 0L;
    }

    private static class TimerData {
        private long cooldownTime;
        private int cooldownTaskId;

        public boolean isRunning() {
            return getTime() != 0;
        }

        public long getTime() {
            return cooldownTime;
        }

        public void setTime(long value) {
            cooldownTime = value;
        }

        public int getTaskId() {
            return cooldownTaskId;
        }

        public void setTaskId(int value) {
            cooldownTaskId = value;
        }
    }
}
