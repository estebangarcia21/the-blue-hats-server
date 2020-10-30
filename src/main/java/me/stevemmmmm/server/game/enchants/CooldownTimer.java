package me.stevemmmmm.server.game.enchants;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.stevemmmmm.server.core.Main;

public class CooldownTimer {
    private Main main;
    private HashMap<UUID, CooldownTimerData> timerData = new HashMap<>();

    public CooldownTimer(Main main) {
        this.main = main;
    }

    public void startCooldown(Player player, long ticks) {
        CooldownTimerData data = timerData.putIfAbsent(player.getUniqueId(), new CooldownTimerData());

        data.setCooldownTaskId(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, () -> {
            data.setCooldownTime(data.getCooldownTime() - 1);

            if (data.getCooldownTime() <= 0) {
                data.setCooldownTime(0);

                Bukkit.getServer().getScheduler().cancelTask(data.getCooldownTaskId());
            }
        }, 0L, 1L));
    }

    public void startCooldown(Player player, long ticks, Runnable post) {
        CooldownTimerData data = timerData.putIfAbsent(player.getUniqueId(), new CooldownTimerData());

        data.setCooldownTaskId(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, () -> {
            data.setCooldownTime(data.getCooldownTime() - 1);

            if (data.getCooldownTime() <= 0) {
                data.setCooldownTime(0);

                post.run();

                Bukkit.getServer().getScheduler().cancelTask(data.getCooldownTaskId());
            }
        }, 0L, 1L));
    }

    public boolean isOnCooldown(Player player) {
        return timerData.get(player.getUniqueId()).isOnCooldown();
    }
}

class CooldownTimerData {
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
