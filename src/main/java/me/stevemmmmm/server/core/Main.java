package me.stevemmmmm.server.core;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.stevemmmmm.server.commands.EnchantCommand;
import me.stevemmmmm.server.commands.GiveFreshItemCommand;
import me.stevemmmmm.server.game.enchants.CustomEnchantManager;
import me.stevemmmmm.server.game.enchants.Peroxide;
import me.stevemmmmm.server.game.enchants.Wasp;
import me.stevemmmmm.server.game.managers.BowManager;
import me.stevemmmmm.server.game.managers.DamageManager;

public class Main extends JavaPlugin implements Registerer {
    @Override
    public void onEnable() {
        Main main = this;

        Logger log = Bukkit.getLogger();

        log.info("------------------------------------------");
        log.info("   The Hypixel Pit Remake by Stevemmmmm   ");
        log.info("------------------------------------------");

        DamageManager damageManager = new DamageManager();
        BowManager bowManager = new BowManager();
        CustomEnchantManager customEnchantManager = new CustomEnchantManager(main);

        registerCommands(main, customEnchantManager);
        registerEnchants(damageManager, bowManager, customEnchantManager);
        registerPerks(damageManager, bowManager, customEnchantManager);
    }

    @Override
    public void onDisable() {

    }

    public void registerEnchants(DamageManager damageManager, BowManager bowManager,
            CustomEnchantManager customEnchantManager) {
        customEnchantManager.registerEnchant(new Wasp(bowManager));
        customEnchantManager.registerEnchant(new Peroxide());
    }

    public void registerPerks(DamageManager damageManager, BowManager bowManager,
            CustomEnchantManager customEnchantManager) {
    }

    public void registerCommands(Main main, CustomEnchantManager customEnchantManager) {
        main.getCommand("pitenchant").setExecutor(new EnchantCommand(customEnchantManager));
        main.getCommand("givefreshitem").setExecutor(new GiveFreshItemCommand());
    }
}
