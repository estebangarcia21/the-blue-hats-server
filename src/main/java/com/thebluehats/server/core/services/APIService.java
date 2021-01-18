package com.thebluehats.server.core.services;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.thebluehats.server.api.listeners.RegisterPlayer;
import org.bukkit.plugin.java.JavaPlugin;

public class APIService implements Service {
    private final JavaPlugin plugin;

    @Inject
    public APIService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void provision(Injector injector) {
        plugin.getServer().getPluginManager().registerEvents(injector.getInstance(RegisterPlayer.class), plugin);
    }
}
