package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import javax.inject.Inject;

import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.PostDamageEventTemplate;
import com.thebluehats.server.game.managers.enchants.CooldownTimer;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.OnDamageEnchant;
import com.thebluehats.server.game.managers.enchants.processedevents.PostDamageEventResult;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Assassin extends OnDamageEnchant {
    private final EnchantProperty<Integer> cooldownTime = new EnchantProperty<>(5, 4, 3);

    private final CooldownTimer cooldownTimer;

    @Inject
    public Assassin(CooldownTimer cooldownTimer, PlayerHitPlayerTemplate playerHitPlayerTemplate,
            ArrowHitPlayerTemplate arrowHitPlayerTemplate) {
        super(new PostDamageEventTemplate[] { playerHitPlayerTemplate, arrowHitPlayerTemplate });

        this.cooldownTimer = cooldownTimer;
    }

    @Override
    public void execute(PostDamageEventResult data) {
        Player damager = data.getDamager();
        Player damagee = data.getDamagee();

        if (!cooldownTimer.isOnCooldown(damagee)) {
            Location tpLoc = damager.getLocation().subtract(damager.getEyeLocation().getDirection().normalize());
            tpLoc.setY(damager.getLocation().getY());

            if (tpLoc.getBlock().getType() == Material.AIR) {
                damagee.teleport(tpLoc);
            } else {
                damagee.teleport(damager);
            }

            damagee.getWorld().playSound(damagee.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 2);
        }

        cooldownTimer.startCooldown(damagee, cooldownTime.getValueAtLevel(data.getLevel()));
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
