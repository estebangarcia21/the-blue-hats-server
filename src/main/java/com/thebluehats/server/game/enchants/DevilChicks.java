package com.thebluehats.server.game.enchants;

import com.google.common.util.concurrent.AtomicDouble;
import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class DevilChicks implements CustomEnchant, Listener {
    private final EnchantProperty<Integer> amountOfChicks = new EnchantProperty<>(1, 2, 3);

    private final HashMap<UUID, Integer> devilchickAnimations = new HashMap<>();

    private final JavaPlugin plugin;
    private final DamageManager damageManager;
    private final CustomEnchantUtils customEnchantUtils;

    @Inject
    public DevilChicks(JavaPlugin plugin, DamageManager damageManager, CustomEnchantUtils customEnchantUtils) {
        this.plugin = plugin;
        this.damageManager = damageManager;
        this.customEnchantUtils = customEnchantUtils;
    }

    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if (projectile.getShooter() instanceof Player) {
            Player shooter = (Player) event.getEntity().getShooter();

            ItemStack bow = shooter.getInventory().getItemInHand();

            if (customEnchantUtils.itemHasEnchant(this, bow)) {
                execute(customEnchantUtils.getEnchantLevel(this, bow), shooter, projectile);
            }
        }
    }

    // TODO Add getValidators to CustomEnchant interface
    // TODO Fix dead-alive bug
    public void execute(int level, Player shooter, Projectile arrow) {
        World world = shooter.getWorld();
        UUID arrowUuid = arrow.getUniqueId();

        Location shooterLocation = shooter.getLocation();
        Location arrowLocation = arrow.getLocation();

        int chickenAmount = amountOfChicks.getValueAtLevel(level);

        final double pitchIncrement = 0.1;
        final float volume = 0.5f;
        final float blastRadius = 0.75f;

        AtomicDouble pitch = new AtomicDouble(0.6);
        AtomicInteger animationIndex = new AtomicInteger();

        Chicken[] chickens = new Chicken[chickenAmount];

        for (int i = 0; i < chickens.length; i++) {
            Vector direction = new Vector();

            direction.setX(arrowLocation.getX() + (Math.random() - Math.random()) * blastRadius);
            direction.setY(arrowLocation.getY());
            direction.setZ(arrowLocation.getZ() + (Math.random() - Math.random()) * blastRadius);

            Chicken chicken = (Chicken) world.spawnEntity(direction.toLocation(arrowLocation.getWorld()), EntityType.CHICKEN);
            chicken.setBaby();

            chickens[i] = chicken;
        }

        int taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (animationIndex.get() == 10) {
                Bukkit.getServer().getScheduler().cancelTask(devilchickAnimations.get(arrowUuid));
                devilchickAnimations.remove(arrowUuid);

                world.playSound(shooterLocation, Sound.CHICKEN_HURT, 1, 2);

                for (Chicken chicken : chickens) {
                    for (Entity entity : chicken.getNearbyEntities(1, 1, 1)) {
                        if (entity instanceof Player) {
                            Player player = (Player) entity;

                            damageManager.doTrueDamage(player, 2.4, player);

                            createExplosion(player, chicken.getLocation());
                        }
                    }

                    world.playSound(arrowLocation, Sound.EXPLODE, volume, 1.6f);
                    world.playEffect(chicken.getLocation(), Effect.EXPLOSION_LARGE, Effect.EXPLOSION_LARGE.getData(), 100);

                    chicken.remove();
                }
            }

            world.playSound(shooterLocation, Sound.NOTE_SNARE_DRUM, volume, (float) pitch.get());

            pitch.addAndGet(pitchIncrement);
            animationIndex.incrementAndGet();
        }, 0L, 1L);

        devilchickAnimations.put(arrowUuid, taskId);
    }

    private void createExplosion(Player target, Location position) {
        Vector explosion = target.getLocation().toVector().subtract(position.toVector()).normalize();

        target.setVelocity(explosion);
    }

    @Override
    public String getName() {
        return "Devil Chicks";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Devilchicks";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("Arrows ");

        enchantLoreParser.addTextIf(level == 1, "spawn with explosive chicken.");
        enchantLoreParser.addTextIf(level == 2, "spawn many explosive chickens.");
        enchantLoreParser.addTextIf(level == 3, "spawn too many explosive chickens.");

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
        return new Material[]{Material.BOW};
    }
}
