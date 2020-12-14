package com.thebluehats.server.game.managers.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.google.inject.Inject;
import com.thebluehats.server.core.modules.annotations.MirrorReference;
import com.thebluehats.server.game.enchants.Mirror;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.game.regionmanager.RegionManager;
import com.thebluehats.server.game.utils.EntityValidator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public class DamageManager implements EntityValidator {
    private final HashMap<UUID, EventData> eventData = new HashMap<>();
    private final ArrayList<UUID> canceledPlayers = new ArrayList<>();
    private final ArrayList<UUID> removeCriticalDamage = new ArrayList<>();

    private final Mirror mirror;
    private final EnchantProperty<Float> mirrorReflectionValues = new EnchantProperty<>(0f, .25f, .5f);

    private final CustomEnchantManager customEnchantManager;
    private final CombatManager combatManager;
    private final CustomEnchantUtils customEnchantUtils;

    @Inject
    public DamageManager(@MirrorReference Mirror mirror, CustomEnchantManager customEnchantManager,
            CombatManager combatManager, CustomEnchantUtils customEnchantUtils) {
        this.customEnchantManager = customEnchantManager;
        this.combatManager = combatManager;
        this.mirror = mirror;
        this.customEnchantUtils = customEnchantUtils;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHit(EntityDamageByEntityEvent event) {
        UUID damagerUuid = event.getDamager().getUniqueId();

        if (eventData.containsKey(damagerUuid))
            return;

        if (canceledPlayers.contains(damagerUuid)) {
            event.setCancelled(true);
        } else {
            event.setDamage(getDamageFromEvent(event));
        }

        eventData.remove(damagerUuid);
        canceledPlayers.remove(damagerUuid);
    }

    public double getDamageFromEvent(EntityDamageByEntityEvent event) {
        return calculateDamage(event.getDamage(), event);
    }

    public double getFinalDamageFromEvent(EntityDamageByEntityEvent event) {
        return calculateDamage(event.getFinalDamage(), event);
    }

    public void addDamage(EntityDamageByEntityEvent event, double value, CalculationMode mode) {
        EventData data = eventData.get(event.getDamager().getUniqueId());

        if (mode == CalculationMode.ADDITIVE) {
            data.setAdditiveDamage(data.getAdditiveDamage() + value);
        }

        if (mode == CalculationMode.MULTIPLICATIVE) {
            data.setMultiplicativeDamage(data.getMultiplicativeDamage() + value);
        }
    }

    public void reduceDamage(EntityDamageByEntityEvent event, double value) {
        EventData data = eventData.get(event.getDamager().getUniqueId());

        if (data.getReductionAmount() == 1) {
            data.setReductionAmount(data.getReductionAmount() - (1 - value));

            return;
        }

        data.setReductionAmount(1 - data.getReductionAmount() * value);
    }

    public void reduceAbsoluteDamage(EntityDamageByEntityEvent event, double value) {
        EventData data = eventData.get(event.getDamager().getUniqueId());

        data.setAbsoluteReductionAmount(data.getAbsoluteReductionAmount() + value);
    }

    public void removeExtraCriticalDamage(EntityDamageByEntityEvent event) {
        removeCriticalDamage.add(event.getDamager().getUniqueId());
    }

    public void setEventAsCanceled(EntityDamageByEntityEvent event) {
        canceledPlayers.add(event.getDamager().getUniqueId());
    }

    public boolean eventIsNotCancelled(EntityDamageByEntityEvent event) {
        return !canceledPlayers.contains(event.getDamager().getUniqueId());
    }

    public boolean playerIsInCanceledEvent(Player player) {
        for (UUID uuid : canceledPlayers) {
            Entity entity = Bukkit.getEntity(uuid);

            if (entity instanceof Projectile) {
                ProjectileSource projectileShooter = ((Projectile) entity).getShooter();

                if (projectileShooter instanceof Player) {
                    if (projectileShooter.equals(player)) {
                        return true;
                    }
                }
            }

            if (entity instanceof Player) {
                if (entity.equals(player)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean arrowIsInCanceledEvent(Arrow projectile) {
        for (UUID uuid : canceledPlayers) {
            Entity entity = Bukkit.getEntity(uuid);

            if (entity instanceof Arrow) {
                if (entity.equals(projectile)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean uuidIsInCanceledEvent(UUID uuid) {
        return canceledPlayers.contains(uuid);
    }

    public void doTrueDamage(Player target, double damage) {
        if (customEnchantUtils.itemHasEnchant(mirror, target.getInventory().getLeggings())) {
            target.setHealth(Math.max(0, target.getHealth() - damage));
            target.damage(0);
        }
    }

    public void doTrueDamage(Player target, double damage, Player reflectTo) {
        int level = customEnchantUtils.getEnchantLevel(mirror, target.getInventory().getLeggings());

        combatManager.combatTag(target);

        if (!customEnchantUtils.itemHasEnchant(mirror, target.getInventory().getLeggings())) {
            if (target.getHealth() - damage < 0) {
                safeSetPlayerHealth(target, 0);
            } else {
                target.damage(0);
                safeSetPlayerHealth(target, target.getHealth() - damage);
            }
        } else if (level != 1) {
            try {
                if (reflectTo.getHealth() - (damage * mirrorReflectionValues.getValueAtLevel(level)) < 0) {
                    safeSetPlayerHealth(target, 0);
                } else {
                    reflectTo.damage(0);

                    combatManager.combatTag(target);

                    safeSetPlayerHealth(reflectTo, Math.max(0,
                            reflectTo.getHealth() - (damage * mirrorReflectionValues.getValueAtLevel(level))));
                }
            } catch (NullPointerException ignored) {
            }
        }
    }

    public void safeSetPlayerHealth(Player player, double health) {
        if (!RegionManager.getInstance().playerIsInRegion(player, RegionManager.RegionType.SPAWN)) {
            if (health >= 0 && health <= player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
                player.setHealth(health);
            }
        }
    }

    public boolean isCriticalHit(Player player) {
        return player.getFallDistance() > 0 && !((Entity) player).isOnGround()
                && player.getLocation().getBlock().getType() != Material.LADDER
                && player.getLocation().getBlock().getType() != Material.VINE
                && player.getLocation().getBlock().getType() != Material.WATER
                && player.getLocation().getBlock().getType() != Material.LAVA && player.getVehicle() == null
                && !player.hasPotionEffect(PotionEffectType.BLINDNESS);
    }

    private double calculateDamage(double initialDamage, EntityDamageByEntityEvent event) {
        EventData data = eventData.get(event.getDamager().getUniqueId());

        double damage = initialDamage * data.getAdditiveDamage() * data.getMultiplicativeDamage()
                * data.getReductionAmount() - data.getAbsoluteReductionAmount();

        if (removeCriticalDamage.contains(event.getDamager().getUniqueId())) {
            // TODO FIX THIS!!
            damage *= .667;
        }

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (player.getInventory().getLeggings() != null) {
                if (player.getInventory().getLeggings().getType() == Material.LEATHER_LEGGINGS) {
                    if (!customEnchantManager.getItemEnchants(player.getInventory().getLeggings()).isEmpty()) {
                        damage *= 0.871;
                    }
                }
            }
        }

        if (damage <= 0)
            damage = 1;

        return damage;
    }

    @Override
    public boolean validate(Entity... entities) {
        for (Entity entity : entities) {
            if (uuidIsInCanceledEvent(entity.getUniqueId())) {
                return false;
            }
        }

        return true;
    }
}

class EventData {
    private double additiveDamage = 1;
    private double multiplicativeDamage;

    private double reductionAmount = 1;
    private double absoluteReductionAmount;

    public double getAdditiveDamage() {
        return additiveDamage;
    }

    public void setAdditiveDamage(double additiveDamage) {
        this.additiveDamage = additiveDamage;
    }

    public double getMultiplicativeDamage() {
        return multiplicativeDamage;
    }

    public void setMultiplicativeDamage(double multiplicativeDamage) {
        this.multiplicativeDamage = multiplicativeDamage;
    }

    public double getReductionAmount() {
        return reductionAmount;
    }

    public void setReductionAmount(double reductionAmount) {
        this.reductionAmount = reductionAmount;
    }

    public double getAbsoluteReductionAmount() {
        return absoluteReductionAmount;
    }

    public void setAbsoluteReductionAmount(double absoluteReductionAmount) {
        this.absoluteReductionAmount = absoluteReductionAmount;
    }
}
