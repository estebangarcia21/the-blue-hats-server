package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;

import com.thebluehats.server.core.TheBlueHatsServerPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginModule extends AbstractModule {
    private final TheBlueHatsServerPlugin plugin;

    public PluginModule(TheBlueHatsServerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(JavaPlugin.class).toInstance(plugin);
        bind(TheBlueHatsServerPlugin.class).toInstance(plugin);
    }
}
