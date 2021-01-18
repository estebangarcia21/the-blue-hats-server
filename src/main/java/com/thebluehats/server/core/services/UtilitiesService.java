package com.thebluehats.server.core.services;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.enchants.GlobalTimer;
import com.thebluehats.server.game.managers.world.PitScoreboard;
import com.thebluehats.server.game.managers.world.WorldSelectionManager;
import com.thebluehats.server.game.other.Bread;
import com.thebluehats.server.game.other.DamageIndicator;
import com.thebluehats.server.game.other.EnderChest;
import com.thebluehats.server.game.other.Obsidian;
import com.thebluehats.server.game.utils.PluginLifecycleListener;
import com.thebluehats.server.game.utils.Registerer;
import com.thebluehats.server.game.world.*;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getServer;

public class UtilitiesService implements Service {
    private final JavaPlugin plugin;
    private final GlobalTimer globalTimer;
    private final Registerer<PluginLifecycleListener> pluginLifecycleListenerRegisterer;

    @Inject
    public UtilitiesService(JavaPlugin plugin, GlobalTimer globalTimer, Registerer<PluginLifecycleListener> pluginLifecycleListenerRegisterer) {
        this.plugin = plugin;
        this.globalTimer = globalTimer;
        this.pluginLifecycleListenerRegisterer = pluginLifecycleListenerRegisterer;
    }

    @Override
    public void provision(Injector injector) {
        getServer().getPluginManager().registerEvents(injector.getInstance(DamageManager.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(WorldSelectionManager.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(PitScoreboard.class), plugin);

        getServer().getPluginManager().registerEvents(injector.getInstance(Bread.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(EnderChest.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(AntiHunger.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(DamageIndicator.class), plugin);

        Obsidian obsidian = injector.getInstance(Obsidian.class);
        getServer().getPluginManager().registerEvents(obsidian, plugin);

        getServer().getPluginManager().registerEvents(injector.getInstance(NoFallDamage.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(AutoRespawn.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(ClearArrows.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(PlayerJoinLeaveMessages.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(WorldProtection.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(StopLiquidFlow.class), plugin);
        getServer().getPluginManager().registerEvents(injector.getInstance(SpawnProtection.class), plugin);

        globalTimer.addListener(injector.getInstance(PlayableArea.class));

        pluginLifecycleListenerRegisterer.register(new PluginLifecycleListener[] { obsidian, globalTimer });
    }
}
