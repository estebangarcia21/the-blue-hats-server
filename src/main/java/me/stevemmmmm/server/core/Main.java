package me.stevemmmmm.server.core;

import me.stevemmmmm.server.game.commands.*;
import me.stevemmmmm.server.game.enchants.*;
import me.stevemmmmm.server.game.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin implements Registerer, PluginInfo {
    @Override
    public void onEnable() {
        Main main = this;

        Logger log = Bukkit.getLogger();

        log.info("------------------------------------------");
        log.info("   The Hypixel Pit Remake by Stevemmmmm   ");
        log.info("------------------------------------------");

        CustomEnchantManager customEnchantManager = new CustomEnchantManager(main);
        DamageManager damageManager = new DamageManager(customEnchantManager, new CombatManager(main));
        CombatManager combatManager = new CombatManager(main);
        BowManager bowManager = new BowManager();
        WorldSelectionManager worldSelectionManager = new WorldSelectionManager(main);
        GrindingSystem grindingSystem = new GrindingSystem();

        registerGameLogic(main, damageManager, combatManager, bowManager, grindingSystem, customEnchantManager,
                worldSelectionManager);
    }

    @Override
    public void onDisable() {

    }

    public void registerGameLogic(Main main, DamageManager damageManager, CombatManager combatManager,
                           BowManager bowManager, GrindingSystem grindingSystem,
                                  CustomEnchantManager customEnchantManager,
                                  WorldSelectionManager worldSelectionManager) {
        // Enchants
        customEnchantManager.registerEnchant(new Wasp(bowManager));
        customEnchantManager.registerEnchant(new Peroxide());
        customEnchantManager.registerEnchant(new SprintDrain());
        customEnchantManager.registerEnchant(new LastStand());
        customEnchantManager.registerEnchant(new ComboSwift(new HitCounter(main)));

        // Perks
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
