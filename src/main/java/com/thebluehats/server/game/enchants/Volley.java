package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.managers.combat.BowManager;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;

public class Volley implements CustomEnchant, Listener {
    private final EnchantProperty<Integer> arrows = new EnchantProperty<>(2, 3, 4);

    private final HashMap<Arrow, Integer> volleyTasks = new HashMap<>();
    private final HashMap<Arrow, Integer> arrowCount = new HashMap<>();

    private final JavaPlugin plugin;
    private final RegionManager regionManager;
    private final BowManager bowManager;
    private final CustomEnchantUtils customEnchantUtils;

    @Inject
    public Volley(JavaPlugin plugin, RegionManager regionManager, BowManager bowManager, CustomEnchantUtils customEnchantUtils) {
        this.plugin = plugin;
        this.regionManager = regionManager;
        this.bowManager = bowManager;
        this.customEnchantUtils = customEnchantUtils;
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (event.getProjectile() instanceof Arrow) {
            Arrow eventArrow = (Arrow) event.getProjectile();

            for (Arrow arrow : volleyTasks.keySet()) {
                if (eventArrow.getShooter().equals(arrow.getShooter())) {
                    return;
                }
            }

            if (eventArrow.getShooter() instanceof Player) {
                Player player = (Player) eventArrow.getShooter();

                ItemStack bow = bowManager.getBowFromArrow(eventArrow);

                if (customEnchantUtils.itemHasEnchant(this, bow)) {
                    execute(customEnchantUtils.getEnchantLevel(this, bow), player, eventArrow, event.getForce());
                }
            }
        }
    }

    public void execute(int level, Player player, Arrow arrow, float force) {
        ItemStack item = player.getInventory().getItemInHand();

        Vector originalVelocity = arrow.getVelocity();

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> volleyTasks.put(arrow,
                Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    if (!regionManager.entityIsInSpawn(player)) {
                        player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);
                        Arrow volleyArrow = player.launchProjectile(Arrow.class);

                        volleyArrow.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(originalVelocity.length()));

                        EntityShootBowEvent event = new EntityShootBowEvent(player, item, volleyArrow, force);
                        plugin.getServer().getPluginManager().callEvent(event);

                        bowManager.registerArrow(volleyArrow, player);

                        arrowCount.put(arrow, arrowCount.getOrDefault(arrow, 1) + 1);

                        if (arrowCount.get(arrow) > arrows.getValueAtLevel(level)) {
                            Bukkit.getServer().getScheduler().cancelTask(volleyTasks.get(arrow));

                            volleyTasks.remove(arrow);
                            arrowCount.remove(arrow);
                        }
                    }
                }, 0L, 1)), 2L);
    }

    @Override
    public String getName() {
        return "Volley";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Volley";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("Shoot <white>{0}</white> arrows at once");

        enchantLoreParser.setSingleVariable("3", "4", "5");

        return enchantLoreParser.parseForLevel(level);
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.B;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.BOW };
    }
}
