package com.thebluehats.server.core;

import org.bukkit.plugin.java.JavaPlugin;

public class TheBlueHatsServerPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        new SpigotApplicationBuilder(this)
                .addService(CustomEnchantService.class)
                .addService(PerksService.class)
                .addService(CommandsService.class)
                .addService(UtilitiesService.class)
                .runApplication();
    }

    @Override
    public void onDisable() { }
}
