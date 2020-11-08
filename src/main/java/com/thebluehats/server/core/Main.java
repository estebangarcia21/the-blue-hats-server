package com.thebluehats.server.core;

import com.thebluehats.server.game.commands.*;
import com.thebluehats.server.game.enchants.*;
import com.thebluehats.server.game.managers.combat.BowManager;
import com.thebluehats.server.game.managers.combat.CombatManager;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.enchants.CooldownTimer;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;
import com.thebluehats.server.game.managers.enchants.HitCounter;
import com.thebluehats.server.game.managers.game.GrindingSystem;
import com.thebluehats.server.game.managers.game.PerkManager;
import com.thebluehats.server.game.managers.game.WorldSelectionManager;
import com.thebluehats.server.game.perks.Vampire;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin implements Registerer, PluginInfo {
    @Override
    public void onEnable() {
        Main main = this;

        Logger log = Bukkit.getLogger();

        log.info("\n" +
                "\n" +
                "  _______ _            ____  _              _    _       _          _____                          \n" +
                " |__   __| |          |  _ \\| |            | |  | |     | |        / ____|                         \n" +
                "    | |  | |__   ___  | |_) | |_   _  ___  | |__| | __ _| |_ ___  | (___   ___ _ ____   _____ _ __ \n" +
                "    | |  | '_ \\ / _ \\ |  _ <| | | | |/ _ \\ |  __  |/ _` | __/ __|  \\___ \\ / _ \\ '__\\ \\ / / _ \\ '__|\n" +
                "    | |  | | | |  __/ | |_) | | |_| |  __/ | |  | | (_| | |_\\__ \\  ____) |  __/ |   \\ V /  __/ |   \n" +
                "    |_|  |_| |_|\\___| |____/|_|\\__,_|\\___| |_|  |_|\\__,_|\\__|___/ |_____/ \\___|_|    \\_/ \\___|_|   \n" +
                "\n");

        log.info("\n" +
                "\n" +
                "  ___        ___ _                                             \n" +
                " | _ )_  _  / __| |_ _____ _____ _ __  _ __  _ __  _ __  _ __  \n" +
                " | _ \\ || | \\__ \\  _/ -_) V / -_) '  \\| '  \\| '  \\| '  \\| '  \\ \n" +
                " |___/\\_, | |___/\\__\\___|\\_/\\___|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|\n" +
                "      |__/                                                     \n" +
                "\n");

        CustomEnchantManager customEnchantManager = new CustomEnchantManager(main);
        DamageManager damageManager = new DamageManager(customEnchantManager, new CombatManager(main));
        CombatManager combatManager = new CombatManager(main);
        BowManager bowManager = new BowManager();
        WorldSelectionManager worldSelectionManager = new WorldSelectionManager(main);
        GrindingSystem grindingSystem = new GrindingSystem();
        PerkManager perkManager = new PerkManager();

        registerGameLogic(main, damageManager, combatManager, bowManager, grindingSystem, customEnchantManager,
                worldSelectionManager, perkManager);
    }

    @Override
    public void onDisable() {

    }

    public void registerGameLogic(Main main, DamageManager damageManager, CombatManager combatManager,
                                  BowManager bowManager, GrindingSystem grindingSystem,
                                  CustomEnchantManager customEnchantManager,
                                  WorldSelectionManager worldSelectionManager, PerkManager perkManager) {
        // Enchants
        customEnchantManager.registerEnchant(new Wasp(bowManager));
        customEnchantManager.registerEnchant(new Peroxide());
        customEnchantManager.registerEnchant(new SprintDrain());
        customEnchantManager.registerEnchant(new LastStand());
        customEnchantManager.registerEnchant(new ComboSwift(new HitCounter(main)));

        // Perks
        perkManager.registerPerk(new Vampire(damageManager));

        // Commands
        getCommand("pitenchant").setExecutor(new EnchantCommand(customEnchantManager));
        getCommand("mysticenchants").setExecutor(new MysticEnchantsCommand(customEnchantManager));
        getCommand("selectworld").setExecutor(new SelectWorldCommand(worldSelectionManager));
        getCommand("pitabout").setExecutor(new PitAboutCommand());
        getCommand("givefreshitem").setExecutor(new GiveFreshItemCommand());
        getCommand("duel").setExecutor(new DuelCommand());
        getCommand("giveprot").setExecutor(new GiveProtCommand());
        getCommand("setgold").setExecutor(new SetGoldCommand(grindingSystem));
        getCommand("givebread").setExecutor(new GiveBreadCommand());
        getCommand("givearrows").setExecutor(new GiveArrowCommand());
        getCommand("giveobsidian").setExecutor(new GiveObsidianCommand());
        getCommand("unenchant").setExecutor(new UnenchantCommand(customEnchantManager));
        getCommand("togglepvp").setExecutor(new TogglePvPCommand(damageManager));

        SpawnCommand spawnCommand = new SpawnCommand(combatManager, new CooldownTimer(main));
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
