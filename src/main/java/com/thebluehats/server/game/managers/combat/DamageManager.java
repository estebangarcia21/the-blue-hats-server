package com.thebluehats.server.game.managers.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.google.inject.Inject;
import com.thebluehats.server.game.enchants.Mirror;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;
import com.thebluehats.server.game.managers.game.RegionManager;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class DamageManager {
    private final HashMap<UUID, EventData> eventData = new HashMap<>();
    private final ArrayList<UUID> canceledPlayers = new ArrayList<>();
//    private final Mirror mirror = new Mirror(null);

    private final CustomEnchantManager customEnchantManager;
    private final CombatManager combatManager;

    @Inject
    public DamageManager(CustomEnchantManager customEnchantManager, CombatManager combatManager) {
        this.customEnchantManager = customEnchantManager;
        this.combatManager = combatManager;
    }

    public boolean playerIsInCanceledEvent(Player player) {
        return canceledPlayers.contains(player.getUniqueId());
    }

    public boolean arrowIsInCanceledEvent(Arrow arrow) {
        if (arrow.getShooter() instanceof Player)
            return canceledPlayers.contains(((Player) arrow.getShooter()).getUniqueId());

        return false;
    }

    // @EventHandler(priority = EventPriority.HIGHEST)
    // public void onHit(EntityDamageByEntityEvent event) {
    // if (!eventData.containsKey(event)) {
    // eventData.put(event, new EventData());
    // }

    // if (canceledEvents.contains(event)) {
    // event.setCancelled(true);
    // } else {
    // event.setDamage(getDamageFromEvent(event));
    // }

    // eventData.remove(event);
    // canceledEvents.remove(event);
    // removeCriticalDamage.remove(event);
    // }

    // public double getDamageFromEvent(EntityDamageByEntityEvent event) {
    // return calculateDamage(event.getDamage(), event);
    // }

    // public double getFinalDamageFromEvent(EntityDamageByEntityEvent event) {
    // return calculateDamage(event.getFinalDamage(), event);
    // }

    // public void addDamage(EntityDamageByEntityEvent event, double value,
    // CalculationMode mode) {
    // EventData data = EventData.fromEvent(event);

    // if (mode == CalculationMode.ADDITIVE) {
    // data.setAdditiveDamage(data.getAdditiveDamage() + value);
    // }

    // if (mode == CalculationMode.MULTIPLICATIVE) {
    // data.setMultiplicativeDamage(data.getMultiplicativeDamage() + value);
    // }
    // }

    // public void reduceDamage(EntityDamageByEntityEvent event, double value) {
    // EventData data = EventData.fromEvent(event);

    // if (data.getReductionAmount() == 1) {
    // data.setReductionAmount(data.getReductionAmount() - (1 - value));
    // return;
    // }

    // data.setReductionAmount(1 - data.getReductionAmount() * value);
    // }

    // public void reduceAbsoluteDamage(EntityDamageByEntityEvent event, double
    // value) {
    // EventData data = EventData.fromEvent(event);

    // data.setAbsoluteReductionAmount(data.getAbsoluteReductionAmount() + value);
    // }

    // public void removeExtraCriticalDamage(EntityDamageByEntityEvent event) {
    // removeCriticalDamage.add(event);
    // }

    // public void setEventAsCanceled(EntityDamageByEntityEvent event) {
    // canceledEvents.add(event);
    // }

    // public boolean isEventNotCancelled(EntityDamageByEntityEvent event) {
    // return !canceledEvents.contains(event);
    // }

    // public boolean playerIsInCanceledEvent(Player player) {
    // for (EntityDamageByEntityEvent event : canceledEvents) {
    // if (event.getDamager() instanceof Projectile) {
    // if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
    // if (((Projectile) event.getDamager()).getShooter().equals(player)) {
    // return true;
    // }
    // }
    // }

    // if (event.getDamager() instanceof Player) {
    // if (event.getDamager().equals(player)) {
    // return true;
    // }
    // }

    // if (event.getEntity() instanceof Player) {
    // if (event.getEntity().equals(player)) {
    // return true;
    // }
    // }
    // }

    // return false;
    // }

    // public boolean arrowIsInCanceledEvent(Arrow projectile) {
    // for (EntityDamageByEntityEvent event : canceledEvents) {
    // if (event.getDamager() instanceof Arrow) {
    // if (event.getDamager().equals(projectile)) {
    // return true;
    // }
    // }

    // if (event.getEntity() instanceof Arrow) {
    // if (event.getEntity().equals(projectile)) {
    // return true;
    // }
    // }
    // }

    // return false;
    // }

//    public void doTrueDamage(Player target, double damage) {
//        if (mirror.itemHasEnchant(target.getInventory().getLeggings())) {
//            target.setHealth(Math.max(0, target.getHealth() - damage));
//            target.damage(0);
//        }
//    }
//
//    public void doTrueDamage(Player target, double damage, Player reflectTo) {
//        int level = mirror.getEnchantLevel(target.getInventory().getLeggings());
//
//        combatManager.combatTag(target);
//
//        if (!mirror.itemHasEnchant(target.getInventory().getLeggings())) {
//            if (target.getHealth() - damage < 0) {
//                safeSetPlayerHealth(target, 0);
//            } else {
//                target.damage(0);
//                safeSetPlayerHealth(target, target.getHealth() - damage);
//            }
//        } else if (level != 1) {
//            try {
//                if (reflectTo.getHealth() - (damage * mirror.DAMAGE_REFLECTION.getValueAtLevel(level)) < 0) {
//                    safeSetPlayerHealth(target, 0);
//                } else {
//                    reflectTo.damage(0);
//
//                    combatManager.combatTag(target);
//
//                    safeSetPlayerHealth(reflectTo, Math.max(0,
//                            reflectTo.getHealth() - (damage * mirror.DAMAGE_REFLECTION.getValueAtLevel(level))));
//                }
//            } catch (NullPointerException ignored) {
//
//            }
//        }
//    }

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

    // private double calculateDamage(double initialDamage,
    // EntityDamageByEntityEvent event) {
    // EventData data = EventData.fromEvent(event);

    // double damage = initialDamage * data.getAdditiveDamage() *
    // data.getMultiplicativeDamage()
    // * data.getReductionAmount() - data.getAbsoluteReductionAmount();

    // if (removeCriticalDamage.contains(event)) {
    // damage *= .667;
    // }

    // if (event.getEntity() instanceof Player) {
    // Player player = (Player) event.getEntity();

    // if (player.getInventory().getLeggings() != null) {
    // if (player.getInventory().getLeggings().getType() ==
    // Material.LEATHER_LEGGINGS) {
    // if
    // (!customEnchantManager.getItemEnchants(player.getInventory().getLeggings()).isEmpty())
    // {
    // // TODO This is the only way to try to make leather pants to iron in 1.8.8
    // // spigot. I need to upgrade to 1.9+ spigot to fix this. This heavily changes
    // // the damage output
    // damage *= 0.871;
    // }
    // }
    // }
    // }

    // if (damage <= 0)
    // damage = 1;

    // return damage;
    // }
}

class EventData {
    private double additiveDamage = 1;
    private double multiplicativeDamage;

    private double reductionAmount = 1;
    private double absoluteReductionAmount;

    private EventData() {
    }

    // public static EventData fromEvent(EntityDamageByEntityEvent event) {
    // if (!eventData.containsKey(event)) {
    // eventData.put(event, new EventData());
    // }

    // return eventData.get(event);
    // }

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
