package me.stevemmmmm.server.core;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.stevemmmmm.server.game.enchants.CustomEnchantManager;
import me.stevemmmmm.server.game.enchants.Wasp;
import me.stevemmmmm.server.game.managers.BowManager;
import me.stevemmmmm.server.game.managers.DamageManager;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Main mainInstance = this;

        Logger log = Bukkit.getLogger();

        log.info("------------------------------------------");
        log.info("   The Hypixel Pit Remake by Stevemmmmm   ");
        log.info("------------------------------------------");

        registerObjects(mainInstance);
    }

    @Override
    public void onDisable() {

    }

    private void registerObjects(Main mainInstance) {
        DamageManager damageManager = new DamageManager();
        BowManager bowManager = new BowManager();
        CustomEnchantManager customEnchantManager = new CustomEnchantManager(mainInstance);

        registerEnchants(damageManager, bowManager, customEnchantManager);
        registerPerks(damageManager, bowManager, customEnchantManager);
    }

    private void registerEnchants(DamageManager damageManager, BowManager bowManager,
            CustomEnchantManager customEnchantManager) {
        customEnchantManager.registerEnchant(new Wasp(bowManager));
    }

    private void registerPerks(DamageManager damageManager, BowManager bowManager,
            CustomEnchantManager customEnchantManager) {
        // TODO Register vampire
    }
}
