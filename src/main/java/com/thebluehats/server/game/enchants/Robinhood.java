package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.managers.combat.BowManager;
import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerifier;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Robinhood implements CustomEnchant, Listener {
    private final EnchantProperty<Float> damageReduction = new EnchantProperty<>(.4f, .5f, .6f);
    private final HashMap<Arrow, Integer> arrowTasks = new HashMap<>();

    private final JavaPlugin plugin;
    private final CustomEnchantUtils customEnchantUtils;
    private final DamageManager damageManager;
    private final BowManager bowManager;
    private final ArrowHitPlayerVerifier arrowHitPlayerVerifier;

    private final double ROBINHOOD_RANGE = 8;

    @Inject
    public Robinhood(JavaPlugin plugin, CustomEnchantUtils customEnchantUtils, DamageManager damageManager, BowManager bowManager, ArrowHitPlayerVerifier arrowHitPlayerVerifier) {
        this.plugin = plugin;
        this.customEnchantUtils = customEnchantUtils;
        this.damageManager = damageManager;
        this.bowManager = bowManager;
        this.arrowHitPlayerVerifier = arrowHitPlayerVerifier;
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Arrow arrow = (Arrow) event.getProjectile();

            ItemStack bow = bowManager.getBowFromArrow(arrow);

            if (customEnchantUtils.itemHasEnchant(this, bow)) {
                execute(customEnchantUtils.getEnchantLevel(this, bow), arrow, (Player) arrow.getShooter(), event.getForce());
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (arrowHitPlayerVerifier.verify(event)) {
            Arrow removal = null;

            for (Map.Entry<Arrow, Integer> entry : arrowTasks.entrySet()) {
                if (entry.getKey() == event.getDamager()) {
                    Arrow arrow = (Arrow) event.getDamager();

                    if (arrow.getShooter() instanceof Player) {
                        Player player = (Player) arrow.getShooter();

                        ItemStack bow = player.getInventory().getItemInHand();

                        if (customEnchantUtils.itemHasEnchant(this, bow)) {
                            damageManager.addDamage(event,
                                    damageReduction.getValueAtLevel(customEnchantUtils.getEnchantLevel(this, bow)),
                                    CalculationMode.ADDITIVE);
                        }
                    }

                    if (!arrow.isValid()) {
                        Bukkit.getServer().getScheduler().cancelTask(arrowTasks.get(arrow));
                        removal = arrow;
                    }
                }
            }

            if (removal != null) arrowTasks.remove(removal);
        }
    }

    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow removal = null;

            for (Map.Entry<Arrow, Integer> entry : arrowTasks.entrySet()) {
                if (entry.getKey() == event.getEntity()) {
                    Arrow arrow = (Arrow) event.getEntity();

                    if (!arrow.isValid()) {
                        Bukkit.getServer().getScheduler().cancelTask(arrowTasks.get(arrow));
                        removal = arrow;
                    }
                }
            }

            if (removal != null) arrowTasks.remove(removal);
        }
    }

    public void execute(int level, Arrow arrow, Player player, float force) {
        if (level == 1 && force < 1) return;

        arrowTasks.put(arrow, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            List<Entity> closestEntities = player.getNearbyEntities(ROBINHOOD_RANGE, ROBINHOOD_RANGE, ROBINHOOD_RANGE);
            ArrayList<Player> closestPlayers = new ArrayList<>();

            for (Entity entity : closestEntities) {
                if (entity instanceof Player) {
                    closestPlayers.add((Player) entity);
                }
            }

            if (closestPlayers.isEmpty()) closestEntities = arrow.getNearbyEntities(ROBINHOOD_RANGE, ROBINHOOD_RANGE, ROBINHOOD_RANGE);

            Player closestPlayer = null;

            for (Entity entity : closestEntities) {
                if (entity instanceof Player) {
                    if (entity != player) {
                        if (closestPlayer == null) {
                            closestPlayer = (Player) entity;

                            continue;
                        }

                        if (player.getLocation().toVector().distance(entity.getLocation().toVector()) < player.getLocation().toVector().distance(closestPlayer.getLocation().toVector())) {
                            closestPlayer = (Player) entity;
                        }
                    }
                }
            }

            if (closestPlayer == null) return;

            Vector arrowVector = arrow.getLocation().toVector();
            Vector closestPlayerVector = closestPlayer.getLocation().toVector();
            closestPlayerVector.setY(closestPlayerVector.getY() + 2);

            Vector direction = closestPlayerVector.subtract(arrowVector).normalize();

            arrow.setVelocity(direction);
        }, 0L, 3L));
    }

    @Override
    public String getName() {
        return "Robinhood";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Robinhood";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("");

        enchantLoreParser.addTextIf(level == 1, "Your charged shots are homing but<br/>deal <red>{0}</red> damage");
        enchantLoreParser.addTextIf(level != 1, "All your shots are homing but<br/>deal <red>{0}</red> damage");

        enchantLoreParser.setSingleVariable("40%", "50%", "60%");

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
