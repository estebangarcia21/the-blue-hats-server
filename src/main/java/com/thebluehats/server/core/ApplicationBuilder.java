package com.thebluehats.server.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thebluehats.server.core.modules.*;
import com.thebluehats.server.core.services.Service;

import java.util.ArrayList;

public class ApplicationBuilder {
    private final ArrayList<Class<? extends Service>> services = new ArrayList<>();

    private final TheBlueHatsServerPlugin plugin;

    public ApplicationBuilder(TheBlueHatsServerPlugin plugin) {
        this.plugin = plugin;
    }

    public ApplicationBuilder addService(Class<? extends Service> clazz) {
        services.add(clazz);

        return this;
    }

    public Application build() {
        Injector injector = provisionInjector();

        for (Class<? extends Service> service : services) {
            injector.getInstance(service).provision(injector);
        }

        return injector.getInstance(Application.class);
    }

    private Injector provisionInjector() {
        return Guice.createInjector(new PluginModule(plugin), new RegionManagerModule(),
                new CustomEnchantManagerModule(), new CombatManagerModule(), new EventVerifiersModule(),
                new DamageManagerModule(), new BowManagerModule(), new TimerModule(), new HitCounterModule(),
                new MirrorModule(), new CustomEnchantUtilsModule(), new ServerAPIModule(),
                new PitDataRepositoryModule(), new RomanNumeralConverterModule(), new PantsDataContainerModule(),
                new PerformanceStatsServiceModule(), new GlobalTimerModule(), new DamageEnchantTriggersModule(),
                new RegistererModule(), new PitScoreboardModule());
    }
}
