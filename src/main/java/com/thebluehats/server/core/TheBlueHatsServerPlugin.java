package com.thebluehats.server.core;

import com.thebluehats.server.game.utils.PluginLifecycleListener;
import com.thebluehats.server.game.utils.Registerer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;

public class TheBlueHatsServerPlugin extends JavaPlugin implements Registerer<PluginLifecycleListener> {
    private final ArrayList<PluginLifecycleListener> lifecycleListeners = new ArrayList<>();

    private SpigotApplication spigotApplication;

    @Override
    public void onEnable() {
        spigotApplication = new SpigotApplication(this)
                .addService(APIService.class)
                .addService(UtilitiesService.class)
                .addService(CustomEnchantService.class)
                .addService(PerksService.class)
                .addService(CommandsService.class);

        spigotApplication.runApplication();
    }

    @Override
    public void onDisable() {
        spigotApplication.endApplication();
    }

    @Override
    public void register(PluginLifecycleListener[] objects) {
        lifecycleListeners.addAll(Arrays.asList(objects));
    }

    public ArrayList<PluginLifecycleListener> getLifecycleListeners() {
        return lifecycleListeners;
    }
}
