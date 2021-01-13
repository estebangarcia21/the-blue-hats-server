package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.managers.combat.BowManager;
import com.thebluehats.server.game.managers.enchants.*;
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.UUID;

public class Explosive implements CustomEnchant, Listener {
    private final EnchantProperty<Double> explosionRange = new EnchantProperty<>(1D, 2.5D, 6D);
    private final EnchantProperty<Integer> cooldownTime = new EnchantProperty<>(5, 3, 5);
    private final EnchantProperty<Float> explosionPitch = new EnchantProperty<>(2f, 1f, 1.4f);

    private final EnchantProperty<Effect> explosionParticle = new EnchantProperty<>(Effect.EXPLOSION_LARGE,
            Effect.EXPLOSION_HUGE, Effect.EXPLOSION_HUGE);

    private final RegionManager regionManager;
    private final CustomEnchantUtils customEnchantUtils;
    private final BowManager bowManager;
    private final Timer<UUID> timer;

    @Inject
    public Explosive(RegionManager regionManager, CustomEnchantUtils customEnchantUtils, BowManager bowManager, Timer<UUID> timer) {
        this.regionManager = regionManager;
        this.customEnchantUtils = customEnchantUtils;
        this.bowManager = bowManager;
        this.timer = timer;
    }

    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();

            if (event.getEntity().getShooter() instanceof Player) {
                ItemStack bow = bowManager.getBowFromArrow(arrow);

                if (customEnchantUtils.itemHasEnchant(this, bow)) {
                    execute(customEnchantUtils.getEnchantLevel(this, bow), (Player) arrow.getShooter(), arrow);
                }
            }
        }
    }

    public void execute(int level, Player shooter, Arrow arrow) {
        UUID playerUuid = shooter.getUniqueId();

        if (!timer.isRunning(playerUuid)) {
            for (Entity entity : arrow.getNearbyEntities(explosionRange.getValueAtLevel(level),
                    explosionRange.getValueAtLevel(level), explosionRange.getValueAtLevel(level))) {

                if (entity instanceof Player) {
                    Player player = (Player) entity;

                    if (regionManager.entityIsInSpawn(player))
                        continue;

                    if (player != shooter) {
                        Vector force = player.getLocation().toVector().subtract(arrow.getLocation().toVector())
                                .normalize().multiply(1.25);
                        force.setY(.85f);

                        player.setVelocity(force);
                    }
                }
            }

            arrow.getWorld().playSound(arrow.getLocation(), Sound.EXPLODE, 0.75f,
                    explosionPitch.getValueAtLevel(level));
            arrow.getWorld().playEffect(arrow.getLocation(), explosionParticle.getValueAtLevel(level),
                    explosionParticle.getValueAtLevel(level).getData(), 100);
        }

        timer.start(playerUuid, cooldownTime.getValueAtLevel(level) * 20, true);
    }

    @Override
    public String getName() {
        return "Explosive";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Explosive";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("Arrows go {0}! ({1}s cooldown)");

        String[][] variables = new String[2][];
        variables[0] = new String[] { "POP", "BANG", "BOOM" };
        variables[1] = new String[] { "5", "3", "5" };

        enchantLoreParser.setVariables(variables);

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