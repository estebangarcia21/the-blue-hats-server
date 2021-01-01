package com.thebluehats.server.core;

import java.util.ArrayList;
import java.util.function.Consumer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thebluehats.server.api.implementations.pitdata.PitDataDaoImpl;
import com.thebluehats.server.core.modules.BowManagerModule;
import com.thebluehats.server.core.modules.CombatManagerModule;
import com.thebluehats.server.core.modules.CooldownTimerModule;
import com.thebluehats.server.core.modules.CustomEnchantManagerModule;
import com.thebluehats.server.core.modules.CustomEnchantUtilsModule;
import com.thebluehats.server.core.modules.DamageManagerModule;
import com.thebluehats.server.core.modules.EventTemplatesModule;
import com.thebluehats.server.core.modules.HitCounterModule;
import com.thebluehats.server.core.modules.MirrorModule;
import com.thebluehats.server.core.modules.PantsDataContainerModule;
import com.thebluehats.server.core.modules.PitDataDaoModule;
import com.thebluehats.server.core.modules.PitDataRepositoryModule;
import com.thebluehats.server.core.modules.PluginModule;
import com.thebluehats.server.core.modules.RegionManagerModule;
import com.thebluehats.server.core.modules.RomanNumeralConverterModule;
import com.thebluehats.server.core.modules.ServerApiModule;
import com.thebluehats.server.game.commands.AboutCommand;
import com.thebluehats.server.game.commands.EnchantCommand;
import com.thebluehats.server.game.commands.GiveArrowCommand;
import com.thebluehats.server.game.commands.GiveBreadCommand;
import com.thebluehats.server.game.commands.GiveFreshItemCommand;
import com.thebluehats.server.game.commands.GiveObsidianCommand;
import com.thebluehats.server.game.commands.GiveProtCommand;
import com.thebluehats.server.game.commands.MysticEnchantsCommand;
import com.thebluehats.server.game.commands.SelectWorldCommand;
import com.thebluehats.server.game.commands.SetGoldCommand;
import com.thebluehats.server.game.commands.SpawnCommand;
import com.thebluehats.server.game.commands.UnenchantCommand;
import com.thebluehats.server.game.enchants.Assassin;
import com.thebluehats.server.game.enchants.BeatTheSpammers;
import com.thebluehats.server.game.enchants.Billionaire;
import com.thebluehats.server.game.enchants.ComboDamage;
import com.thebluehats.server.game.enchants.ComboSwift;
import com.thebluehats.server.game.enchants.DiamondAllergy;
import com.thebluehats.server.game.enchants.DiamondStomp;
import com.thebluehats.server.game.enchants.Fletching;
import com.thebluehats.server.game.enchants.FractionalReserve;
import com.thebluehats.server.game.enchants.Healer;
import com.thebluehats.server.game.enchants.KingBuster;
import com.thebluehats.server.game.enchants.Knockback;
import com.thebluehats.server.game.enchants.LastStand;
import com.thebluehats.server.game.enchants.Mirror;
import com.thebluehats.server.game.enchants.Parasite;
import com.thebluehats.server.game.enchants.Peroxide;
import com.thebluehats.server.game.enchants.Punisher;
import com.thebluehats.server.game.enchants.Solitude;
import com.thebluehats.server.game.enchants.SprintDrain;
import com.thebluehats.server.game.enchants.Wasp;
import com.thebluehats.server.game.managers.combat.CombatManager;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;
import com.thebluehats.server.game.managers.world.PerkManager;
import com.thebluehats.server.game.managers.world.WorldSelectionManager;
import com.thebluehats.server.game.perks.Perk;
import com.thebluehats.server.game.perks.Vampire;
import com.thebluehats.server.game.utils.PluginLifecycleListener;
import com.thebluehats.server.game.utils.Registerer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private final ArrayList<PluginLifecycleListener> lifecycleListeners = new ArrayList<>();

    private Injector injector;

    @Override
    public void onEnable() {
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

        injector = Guice.createInjector(new PluginModule(this), new RegionManagerModule(),
                new CustomEnchantManagerModule(), new CombatManagerModule(), new EventTemplatesModule(),
                new DamageManagerModule(), new BowManagerModule(), new CooldownTimerModule(), new HitCounterModule(),
                new MirrorModule(), new CustomEnchantUtilsModule(), new ServerApiModule(),
                new PitDataRepositoryModule(), new RomanNumeralConverterModule(), new PantsDataContainerModule(),
                new PitDataDaoModule());

        registerLifecycles();
        registerEvents();
        registerEnchants();
        registerPerks();
        registerCommands();

        updateLifecyles(PluginLifecycleListener::onPluginStart);
    }

    @Override
    public void onDisable() {
        updateLifecyles(PluginLifecycleListener::onPluginEnd);
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(injector.getInstance(CombatManager.class), this);
        getServer().getPluginManager().registerEvents(injector.getInstance(DamageManager.class), this);
        getServer().getPluginManager().registerEvents(injector.getInstance(WorldSelectionManager.class), this);
    }

    private void registerEnchants() {
        Registerer<CustomEnchant> customEnchantRegisterer = injector.getInstance(CustomEnchantManager.class);

        customEnchantRegisterer.register(injector.getInstance(BeatTheSpammers.class));
        customEnchantRegisterer.register(injector.getInstance(Wasp.class));
        customEnchantRegisterer.register(injector.getInstance(Peroxide.class));
        customEnchantRegisterer.register(injector.getInstance(SprintDrain.class));
        customEnchantRegisterer.register(injector.getInstance(ComboSwift.class));
        customEnchantRegisterer.register(injector.getInstance(ComboDamage.class));
        customEnchantRegisterer.register(injector.getInstance(Healer.class));
        customEnchantRegisterer.register(injector.getInstance(LastStand.class));
        customEnchantRegisterer.register(injector.getInstance(Mirror.class));
        customEnchantRegisterer.register(injector.getInstance(Billionaire.class));
        customEnchantRegisterer.register(injector.getInstance(DiamondStomp.class));
        customEnchantRegisterer.register(injector.getInstance(Punisher.class));
        customEnchantRegisterer.register(injector.getInstance(DiamondAllergy.class));
        customEnchantRegisterer.register(injector.getInstance(Solitude.class));
        customEnchantRegisterer.register(injector.getInstance(Assassin.class));
        customEnchantRegisterer.register(injector.getInstance(Fletching.class));
        customEnchantRegisterer.register(injector.getInstance(KingBuster.class));
        customEnchantRegisterer.register(injector.getInstance(Knockback.class));
        customEnchantRegisterer.register(injector.getInstance(Parasite.class));
        customEnchantRegisterer.register(injector.getInstance(FractionalReserve.class));
    }

    private void registerPerks() {
        Registerer<Perk> perkRegisterer = new PerkManager();

        perkRegisterer.register(injector.getInstance(Vampire.class));
    }

    private void registerCommands() {
        injector.getInstance(SpawnCommand.class).registerCommand(this);
        injector.getInstance(EnchantCommand.class).registerCommand(this);
        injector.getInstance(MysticEnchantsCommand.class).registerCommand(this);
        injector.getInstance(SelectWorldCommand.class).registerCommand(this);
        injector.getInstance(SetGoldCommand.class).registerCommand(this);
        injector.getInstance(UnenchantCommand.class).registerCommand(this);
        injector.getInstance(AboutCommand.class).registerCommand(this);
        injector.getInstance(GiveFreshItemCommand.class).registerCommand(this);
        injector.getInstance(GiveProtCommand.class).registerCommand(this);
        injector.getInstance(GiveBreadCommand.class).registerCommand(this);
        injector.getInstance(GiveArrowCommand.class).registerCommand(this);
        injector.getInstance(GiveObsidianCommand.class).registerCommand(this);
    }

    private void registerLifecycles() {
        lifecycleListeners.add(injector.getInstance(PitDataDaoImpl.class));
    }

    private void updateLifecyles(Consumer<PluginLifecycleListener> action) {
        for (PluginLifecycleListener lifecycle : lifecycleListeners) {
            action.accept(lifecycle);
        }
    }
}
