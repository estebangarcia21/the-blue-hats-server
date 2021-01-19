package com.thebluehats.server.core;

import com.google.inject.Inject;
import com.thebluehats.server.game.utils.PluginLifecycleListener;
import org.bukkit.Bukkit;

public class Application {
    private final TheBlueHatsServerPlugin plugin;

    @Inject
    public Application(TheBlueHatsServerPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        plugin.getLifecycleListeners().forEach(PluginLifecycleListener::onPluginStart);

        logInitializationMessage();
    }

    public void stop() {
        plugin.getLifecycleListeners().forEach(PluginLifecycleListener::onPluginEnd);
    }

    private void logInitializationMessage() {
        Bukkit.getLogger().info("\n" + "\n"
            + "  _______ _            ____  _              _    _       _          _____                          \n"
            + " |__   __| |          |  _ \\| |            | |  | |     | |        / ____|                         \n"
            + "    | |  | |__   ___  | |_) | |_   _  ___  | |__| | __ _| |_ ___  | (___   ___ _ ____   _____ _ __ \n"
            + "    | |  | '_ \\ / _ \\ |  _ <| | | | |/ _ \\ |  __  |/ _` | __/ __|  \\___ \\ / _ \\ '__\\ \\ / / _ \\ '__|\n"
            + "    | |  | | | |  __/ | |_) | | |_| |  __/ | |  | | (_| | |_\\__ \\  ____) |  __/ |   \\ V /  __/ |   \n"
            + "    |_|  |_| |_|\\___| |____/|_|\\__,_|\\___| |_|  |_|\\__,_|\\__|___/ |_____/ \\___|_|    \\_/ \\___|_|   \n"
            + "\n" + "   ___        ___ _                                             \n"
            + "  | _ )_  _  / __| |_ _____ _____ _ __  _ __  _ __  _ __  _ __  \n"
            + "  | _ \\ || | \\__ \\  _/ -_) V / -_) '  \\| '  \\| '  \\| '  \\| '  \\ \n"
            + "  |___/\\_, | |___/\\__\\___|\\_/\\___|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|\n"
            + "       |__/                                                     \n");
    }
}
