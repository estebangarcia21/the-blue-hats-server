package me.stevemmmmm.server.core;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Main mainInstance = this;

        Logger log = Bukkit.getLogger();

        log.info("------------------------------------------");
        log.info("   The Hypixel Pit Remake by Stevemmmmm   ");
        log.info("------------------------------------------");

        new ListenerRegisterer().registerObjects(mainInstance);
    }

    @Override
    public void onDisable() {

    }
}
