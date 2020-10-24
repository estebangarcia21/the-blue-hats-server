package me.stevemmmmm.server.core;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        Logger log = Bukkit.getLogger();

        log.info("------------------------------------------");
        log.info("The Hypixel Pit Remake by Stevemmmmm");
        log.info("------------------------------------------");
    }

    @Override
    public void onDisable() {

    }
}
