package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerificationTemplate;
import com.thebluehats.server.game.managers.combat.templates.DamageEventVerificationTemplate;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.processedevents.CastedEntityDamageByEntityEvent;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Healer extends DamageTriggeredEnchant {
    private final EnchantProperty<Integer> healAmount = new EnchantProperty<>(2, 4, 6);

    @Inject
    public Healer(PlayerHitPlayerVerificationTemplate playerHitPlayerTemplate) {
        super(new DamageEventVerificationTemplate[] { playerHitPlayerTemplate });
    }

    @Override
    public void execute(CastedEntityDamageByEntityEvent data) {
        Player damager = data.getDamager();
        Player damaged = data.getDamagee();
        int level = data.getLevel();

        damager.setHealth(Math.min(damager.getHealth() + healAmount.getValueAtLevel(level), damager.getMaxHealth()));
        damaged.setHealth(Math.min(damaged.getHealth() + healAmount.getValueAtLevel(level), damaged.getMaxHealth()));
    }

    @Override
    public String getName() {
        return "Healer";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Healer";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Hitting a player <green>heals</green> both you adn them for <red>{0}</red>");

        enchantLoreParser.setSingleVariable("1❤", "2❤", "3❤");

        return enchantLoreParser.parseForLevel(level);
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return true;
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
        return new Material[] { Material.GOLD_SWORD };
    }

    @Override
    public EnchantHolder getEnchantHolder() {
        return EnchantHolder.DAMAGER;
    }
}