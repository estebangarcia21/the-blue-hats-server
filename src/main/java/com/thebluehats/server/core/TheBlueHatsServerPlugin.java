package com.thebluehats.server.core;

import org.bukkit.plugin.java.JavaPlugin;

public class TheBlueHatsServerPlugin extends JavaPlugin {
    private SpigotApplication spigotApplication;

    @Override
    public void onEnable() {
        spigotApplication = new SpigotApplication(this)
                .addService(CustomEnchantService.class)
                .addService(PerksService.class)
                .addService(CommandsService.class)
                .addService(UtilitiesService.class);

        spigotApplication.runApplication();
    }

    @Override
    public void onDisable() {
        spigotApplication.endApplication();
    }
}
