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

                post.run();

                Bukkit.getServer().getScheduler().cancelTask(data.getTaskId());
            }
        }, 0L, 1L));
    }

    public void cancel(K key) {
        TimerData data = timerData.computeIfAbsent(key, k -> new TimerData());

        Bukkit.getServer().getScheduler().cancelTask(data.getTaskId());
    }

    public Set<K> getKeys() {
        return timerData.keySet();
    }

    public boolean isRunning(K key) {
        return timerData.computeIfAbsent(key, k -> new TimerData()).isRunning();
    }

    protected static class TimerData {
        private long cooldownTime;
        private int cooldownTaskId;

        public TimerData() {
            this.cooldownTime = 0;
            this.cooldownTaskId = 0;
        }

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
