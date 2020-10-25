package me.stevemmmmm.server.game.enchants;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.stevemmmmm.server.game.managers.BowManager;
import me.stevemmmmm.server.game.utils.LoreBuilder;

public class Wasp extends CustomEnchant {
    private final EnchantProperty<Integer> weaknessAmplifier = new EnchantProperty<>(1, 2, 3);
    private final EnchantProperty<Integer> duration = new EnchantProperty<>(6, 11, 16);

    private BowManager bowManager;

    public Wasp(BowManager bowManager) {
        this.bowManager = bowManager;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow) || !(event.getEntity() instanceof Player))
            return;

        Arrow arrow = (Arrow) event.getDamager();

        if (!(arrow.getShooter() instanceof Player))
            return;

        Player hitPlayer = (Player) event.getEntity();

        attemptEnchantExecution(new EnchantProcData(bowManager.getBowFromArrow(arrow), new Entity[] { hitPlayer }),
                level -> {
                    hitPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
                            duration.getValueAtLevel(level), weaknessAmplifier.getValueAtLevel(level), true));
                });
    }

    @EventHandler
    public void onArrowShootEvent(EntityShootBowEvent event) {
        bowManager.onArrowShoot(event);
    }

    @Override
    public String getName() {
        return "Wasp";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Wasp";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("II", "III", "IV").setColor(ChatColor.GRAY).write("Apply ")
                .setColor(ChatColor.RED).write("Weakness ").writeVariable(0, level).setColor(ChatColor.GRAY)
                .write(" (" + duration.getValueAtLevel(level) + "s) on hit").build();
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
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.BOW };
    }
}
