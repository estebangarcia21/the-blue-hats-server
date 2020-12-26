package com.thebluehats.server.core;

import java.util.ArrayList;
import java.util.function.Consumer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thebluehats.server.core.modules.BowManagerModule;
import com.thebluehats.server.core.modules.CombatManagerModule;
import com.thebluehats.server.core.modules.CooldownTimerModule;
import com.thebluehats.server.core.modules.CustomEnchantManagerModule;
import com.thebluehats.server.core.modules.CustomEnchantUtilsModule;
import com.thebluehats.server.core.modules.DamageManagerModule;
import com.thebluehats.server.core.modules.EventTemplatesModule;
import com.thebluehats.server.core.modules.PitDataDAOModule;
import com.thebluehats.server.core.modules.HitCounterModule;
import com.thebluehats.server.core.modules.MirrorModule;
import com.thebluehats.server.core.modules.PantsDataContainerModule;
import com.thebluehats.server.core.modules.PitDataRepositoryModule;
import com.thebluehats.server.core.modules.PluginModule;
import com.thebluehats.server.core.modules.RegionManagerModule;
import com.thebluehats.server.core.modules.RomanNumeralConverterModule;
import com.thebluehats.server.core.modules.ServerApiModule;
import com.thebluehats.server.game.commands.AboutCommand;
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
import com.thebluehats.server.game.enchants.Billionaire;
import com.thebluehats.server.game.enchants.ComboDamage;
import com.thebluehats.server.game.enchants.ComboSwift;
import com.thebluehats.server.game.enchants.DiamondAllergy;
import com.thebluehats.server.game.enchants.DiamondStomp;
import com.thebluehats.server.game.enchants.LastStand;
import com.thebluehats.server.game.enchants.Mirror;
import com.thebluehats.server.game.enchants.Peroxide;
import com.thebluehats.server.game.enchants.Punisher;
import com.thebluehats.server.game.enchants.Solitude;
import com.thebluehats.server.game.enchants.SprintDrain;
import com.thebluehats.server.game.enchants.Wasp;
import com.thebluehats.server.game.managers.combat.CombatManager;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;
import com.thebluehats.server.game.managers.world.PerkManager;
import com.thebluehats.server.game.managers.world.WorldSelectionManager;
import com.thebluehats.server.api.implementations.pitdata.PitDataDaoImpl;
import com.thebluehats.server.game.perks.Vampire;
import com.thebluehats.server.game.utils.PluginLifecycleListener;

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
                new PitDataDAOModule());

        registerLifecycles();
        registerEvents();
        registerEnchants();
        registerPerks();
        registerCommands();

        updateLifecyles(lifecycle -> lifecycle.onPluginStart());
    }

    @Override
    public void onDisable() {
        updateLifecyles(lifecycle -> lifecycle.onPluginEnd());
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(injector.getInstance(CombatManager.class), this);
        getServer().getPluginManager().registerEvents(injector.getInstance(WorldSelectionManager.class), this);
    }

    private void registerEnchants() {
        CustomEnchantManager customEnchantManager = injector.getInstance(CustomEnchantManager.class);

        customEnchantManager.registerEnchant(injector.getInstance(Wasp.class));
        customEnchantManager.registerEnchant(injector.getInstance(Peroxide.class));
        customEnchantManager.registerEnchant(injector.getInstance(SprintDrain.class));
        customEnchantManager.registerEnchant(injector.getInstance(ComboSwift.class));
        customEnchantManager.registerEnchant(injector.getInstance(ComboDamage.class));
        customEnchantManager.registerEnchant(injector.getInstance(LastStand.class));
        customEnchantManager.registerEnchant(injector.getInstance(Mirror.class));
        customEnchantManager.registerEnchant(injector.getInstance(Billionaire.class));
        customEnchantManager.registerEnchant(injector.getInstance(DiamondStomp.class));
        customEnchantManager.registerEnchant(injector.getInstance(Punisher.class));
        customEnchantManager.registerEnchant(injector.getInstance(DiamondAllergy.class));
        customEnchantManager.registerEnchant(injector.getInstance(Solitude.class));
    }

    private void registerPerks() {
        PerkManager perkManager = new PerkManager();

        perkManager.registerPerk(injector.getInstance(Vampire.class));
    }

    private void registerCommands() {
        injector.getInstance(SpawnCommand.class).registerCommand(this);
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
