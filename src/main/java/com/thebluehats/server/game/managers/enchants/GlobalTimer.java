package com.thebluehats.server.game.managers.enchants;

import com.thebluehats.server.game.utils.PluginLifecycleListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.ArrayList;

public class GlobalTimer implements PluginLifecycleListener {
    private final ArrayList<GlobalTimerListener> listeners = new ArrayList<>();

    private final JavaPlugin plugin;

    @Inject
    public GlobalTimer(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void addListener(GlobalTimerListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onPluginStart() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (GlobalTimerListener listener : listeners) {
                    listener.onTick(player);
                }
            }
        }, 0L, 1L);
    }

    @Override
    public void onPluginEnd() { }
}
