package com.thebluehats.server.core;

import com.thebluehats.server.core.services.*;
import com.thebluehats.server.game.utils.PluginLifecycleListener;
import com.thebluehats.server.game.utils.Registerer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;

public class TheBlueHatsServerPlugin extends JavaPlugin implements Registerer<PluginLifecycleListener> {
    private final ArrayList<PluginLifecycleListener> lifecycleListeners = new ArrayList<>();

    private Application app;

    @Override
    public void onEnable() {
        app = new ApplicationBuilder(this)
                .addService(APIService.class)
                .addService(UtilitiesService.class)
                .addService(CustomEnchantService.class)
                .addService(PerksService.class)
                .addService(CommandsService.class)
                .build();

        app.start();
    }

    @Override
    public void onDisable() {
        app.stop();
    }

    @Override
    public void register(PluginLifecycleListener[] objects) {
        lifecycleListeners.addAll(Arrays.asList(objects));
    }

    public ArrayList<PluginLifecycleListener> getLifecycleListeners() {
        return lifecycleListeners;
    }
}
