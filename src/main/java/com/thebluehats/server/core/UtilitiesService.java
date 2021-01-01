package com.thebluehats.server.core;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.thebluehats.server.game.managers.combat.CombatManager;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.enchants.GlobalTimer;
import com.thebluehats.server.game.managers.world.PitScoreboard;
import com.thebluehats.server.game.managers.world.WorldSelectionManager;
import com.thebluehats.server.game.other.Bread;
import com.thebluehats.server.game.other.EnderChest;
import com.thebluehats.server.game.other.Obsidian;
import com.thebluehats.server.game.world.*;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getServer;

public class UtilitiesService implements Service {
    private final JavaPlugin plugin;

    @Inject
    public UtilitiesService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run(Injector injector) {
        GlobalTimer globalTimer = injector.getInstance(GlobalTimer.class);

        getServer().getPluginManager().registerEvents(injector.getInstance(CombatManager.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(DamageManager.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(WorldSelectionManager.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(PitScoreboard.class), plugin);

        getServer().getPluginManager().registerEvents(injector.getInstance(Bread.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(EnderChest.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(AntiHunger.class), plugin);

        Obsidian obsidian = injector.getInstance(Obsidian.class);
        getServer().getPluginManager().registerEvents(obsidian, plugin);

        getServer().getPluginManager().registerEvents(injector.getInstance(NoFallDamage.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(AutoRespawn.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(ClearArrows.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(PlayerJoinLeaveMessages.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(WorldProtection.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(StopLiquidFlow.class), plugin);

        globalTimer.addListener(injector.getInstance(PlayableArea.class));

        // TODO
//        lifecycleListeners.add(obsidian);
//        lifecycleListeners.add(globalTimer);
    }
}
