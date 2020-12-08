package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginModule extends AbstractModule {
    private final JavaPlugin plugin;

    public PluginModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(JavaPlugin.class).toInstance(plugin);
    }
}
