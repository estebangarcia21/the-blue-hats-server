package me.stevemmmmm.server.game.enchants;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.stevemmmmm.server.core.Main;

public class CooldownTimer {
    private Main mainInstance;
    private HashMap<UUID, CooldownTimerData> timerData;

    public CooldownTimer(Main mainInstance) {
        this.mainInstance = mainInstance;
    }

    public void startCooldown(Player player, long ticks, boolean isSeconds) {
        if (isSeconds)
            ticks *= 20;

        CooldownTimerData data = timerData.putIfAbsent(player.getUniqueId(), new CooldownTimerData());

        data.setCooldownTaskId(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(mainInstance, () -> {
            data.setCooldownTime(data.getCooldownTime() - 1);

            if (data.getCooldownTime() <= 0) {
                data.setCooldownTime(0);

                Bukkit.getServer().getScheduler().cancelTask(data.getCooldownTaskId());
            }
        }, 0L, 1L));
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
