package com.thebluehats.server.core.services;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.thebluehats.server.game.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandsService implements Service {
    private final JavaPlugin plugin;

    @Inject
    public CommandsService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void provision(Injector injector) {
        injector.getInstance(SpawnCommand.class).registerCommand(plugin);
        injector.getInstance(EnchantCommand.class).registerCommand(plugin);
        injector.getInstance(MysticEnchantsCommand.class).registerCommand(plugin);
        injector.getInstance(SelectWorldCommand.class).registerCommand(plugin);
        injector.getInstance(SetGoldCommand.class).registerCommand(plugin);
        injector.getInstance(UnenchantCommand.class).registerCommand(plugin);
        injector.getInstance(AboutCommand.class).registerCommand(plugin);
        injector.getInstance(GiveFreshItemCommand.class).registerCommand(plugin);
        injector.getInstance(GiveProtCommand.class).registerCommand(plugin);
        injector.getInstance(GiveBreadCommand.class).registerCommand(plugin);
        injector.getInstance(GiveArrowCommand.class).registerCommand(plugin);
        injector.getInstance(GiveObsidianCommand.class).registerCommand(plugin);
    }
}
