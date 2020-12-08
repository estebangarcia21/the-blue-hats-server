package com.thebluehats.server.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thebluehats.server.core.modules.BowManagerModule;
import com.thebluehats.server.core.modules.CombatManagerModule;
import com.thebluehats.server.core.modules.CooldownTimerModule;
import com.thebluehats.server.core.modules.CustomEnchantManagerModule;
import com.thebluehats.server.core.modules.DamageManagerModule;
import com.thebluehats.server.core.modules.EventTemplatesModule;
import com.thebluehats.server.core.modules.HitCounterModule;
import com.thebluehats.server.core.modules.PluginModule;
import com.thebluehats.server.game.commands.DuelCommand;
import com.thebluehats.server.game.commands.EnchantCommand;
import com.thebluehats.server.game.commands.GiveArrowCommand;
import com.thebluehats.server.game.commands.GiveBreadCommand;
import com.thebluehats.server.game.commands.GiveFreshItemCommand;
import com.thebluehats.server.game.commands.GiveObsidianCommand;
import com.thebluehats.server.game.commands.GiveProtCommand;
import com.thebluehats.server.game.commands.MysticEnchantsCommand;
import com.thebluehats.server.game.commands.PitAboutCommand;
import com.thebluehats.server.game.commands.SelectWorldCommand;
import com.thebluehats.server.game.commands.SetGoldCommand;
import com.thebluehats.server.game.commands.SpawnCommand;
import com.thebluehats.server.game.commands.UnenchantCommand;
import com.thebluehats.server.game.enchants.ComboSwift;
import com.thebluehats.server.game.enchants.LastStand;
import com.thebluehats.server.game.enchants.Peroxide;
import com.thebluehats.server.game.enchants.SprintDrain;
import com.thebluehats.server.game.enchants.Wasp;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;
import com.thebluehats.server.game.managers.game.PerkManager;
import com.thebluehats.server.game.perks.Vampire;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements PluginInformationProvider {
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

        Injector injector = Guice.createInjector(new PluginModule(this), new CustomEnchantManagerModule(),
                new CombatManagerModule(), new EventTemplatesModule(), new DamageManagerModule(),
                new BowManagerModule(), new CooldownTimerModule(), new HitCounterModule());

        registerEnchants(injector);
        registerPerks(injector);
        registerCommands(injector);
    }

    @Override
    public void onDisable() {
    }

    private void registerEnchants(Injector injector) {
        CustomEnchantManager customEnchantManager = injector.getInstance(CustomEnchantManager.class);

        customEnchantManager.registerEnchant(injector.getInstance(Wasp.class));
        customEnchantManager.registerEnchant(injector.getInstance(Peroxide.class));
        customEnchantManager.registerEnchant(injector.getInstance(SprintDrain.class));
        customEnchantManager.registerEnchant(injector.getInstance(ComboSwift.class));
        customEnchantManager.registerEnchant(injector.getInstance(LastStand.class));
    }

    private void registerPerks(Injector injector) {
        PerkManager perkManager = new PerkManager();

        perkManager.registerPerk(injector.getInstance(Vampire.class));
    }

    private void registerCommands(Injector injector) {
        getCommand("pitenchant").setExecutor(injector.getInstance(EnchantCommand.class));
        getCommand("mysticenchants").setExecutor(injector.getInstance(MysticEnchantsCommand.class));
        getCommand("selectworld").setExecutor(injector.getInstance(SelectWorldCommand.class));
        getCommand("setgold").setExecutor(injector.getInstance(SetGoldCommand.class));
        getCommand("unenchant").setExecutor(injector.getInstance(UnenchantCommand.class));

        getCommand("pitabout").setExecutor(new PitAboutCommand());
        getCommand("givefreshitem").setExecutor(new GiveFreshItemCommand());
        getCommand("duel").setExecutor(new DuelCommand());
        getCommand("giveprot").setExecutor(new GiveProtCommand());
        getCommand("givebread").setExecutor(new GiveBreadCommand());
        getCommand("givearrows").setExecutor(new GiveArrowCommand());
        getCommand("giveobsidian").setExecutor(new GiveObsidianCommand());

        SpawnCommand spawnCommand = injector.getInstance(SpawnCommand.class);
        getCommand("spawn").setExecutor(spawnCommand);
        getCommand("respawn").setExecutor(spawnCommand);
    }

    @Override
    public String getPluginName() {
        return "The Blue Hats Pit";
    }

    @Override
    public String getVersion() {
        return "v2.0";
    }

    @Override
    public String getDiscord() {
        return "Stevemmmmm#8894";
    }
}
