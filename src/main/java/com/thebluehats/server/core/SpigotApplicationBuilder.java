package com.thebluehats.server.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thebluehats.server.core.modules.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class SpigotApplicationBuilder {
    private final ArrayList<Class<? extends Service>> services = new ArrayList<>();

    private final JavaPlugin plugin;

    public SpigotApplicationBuilder(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public SpigotApplicationBuilder addService(Class<? extends Service> clazz) {
        services.add(clazz);

        return this;
    }

    public void runApplication() {
        Injector injector = provisionInjector();

        for (Class<? extends Service> service : services) {
            injector.getInstance(service).run(injector);
        }

        logInitializationMessage();
    }

    private Injector provisionInjector() {
        return Guice.createInjector(new PluginModule(plugin), new RegionManagerModule(),
                new CustomEnchantManagerModule(), new CombatManagerModule(), new EventVerifiersModule(),
                new DamageManagerModule(), new BowManagerModule(), new TimerModule(), new HitCounterModule(),
                new MirrorModule(), new CustomEnchantUtilsModule(), new ServerApiModule(),
                new PitDataRepositoryModule(), new RomanNumeralConverterModule(), new PantsDataContainerModule(),
                new PitDataDaoModule(), new GlobalTimerModule(), new DamageEnchantTriggersModule());
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
