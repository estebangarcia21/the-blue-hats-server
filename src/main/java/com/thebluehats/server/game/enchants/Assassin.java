package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import javax.inject.Inject;

import com.thebluehats.server.game.managers.combat.templates.*;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.Timer;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Assassin extends DamageTriggeredEnchant {
    private final EnchantProperty<Integer> cooldownTime = new EnchantProperty<>(5, 4, 3);

    private final Timer<Player> timer;

    @Inject
    public Assassin(Timer<Player> timer, PlayerDamageTrigger playerDamageTrigger, ArrowDamageTrigger arrowDamageTrigger) {
        super(new DamageEnchantTrigger[] { playerDamageTrigger, arrowDamageTrigger });

        this.timer = timer;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        Player damager = data.getDamager();
        Player damagee = data.getDamagee();

        if (!timer.isRunning(damagee)) {
            Location tpLoc = damager.getLocation().subtract(damager.getEyeLocation().getDirection().normalize());
            tpLoc.setY(damager.getLocation().getY());

            if (tpLoc.getBlock().getType() == Material.AIR) {
                damagee.teleport(tpLoc);
            } else {
                damagee.teleport(damager);
            }

            damagee.getWorld().playSound(damagee.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 2);
        }

        timer.start(damagee, cooldownTime.getValueAtLevel(data.getLevel()) * 20, false);
    }

    @Override
    public String getName() {
        return "Assassin";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Assassin";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        // TODO Level one is only on arrow
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Sneaking teleports you behind<br/>your<br/>attacker ({0}s cooldown)");

        enchantLoreParser.setSingleVariable("5", "4", "3");

        return enchantLoreParser.parseForLevel(level);
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        // TODO Determine group
        return EnchantGroup.A;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.LEATHER_LEGGINGS };
    }

    @Override
    public EnchantHolder getEnchantHolder() {
        return EnchantHolder.DAMAGEE;
    }
}
