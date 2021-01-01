package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger;
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Parasite extends DamageTriggeredEnchant {
    private final EnchantProperty<Double> healAmount = new EnchantProperty<>(0.5D, 1.0D, 2.0D);

    @Inject
    public Parasite(ArrowDamageTrigger arrowDamageTrigger) {
        super(new DamageEnchantTrigger[] { arrowDamageTrigger });
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        Player damager = data.getDamager();
        int level = data.getLevel();

        damager.setHealth(Math.min(damager.getHealth() + healAmount.getValueAtLevel(level), damager.getMaxHealth()));
    }

    @Override
    public String getName() {
        return "Parasite";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Parasite";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("Heal <red>{0}</red> on arrow hit");

        enchantLoreParser.setSingleVariable("0.25❤", "0.5❤", "1.0❤");

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
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.BOW };
    }

    @Override
    public EnchantHolder getEnchantHolder() {
        return EnchantHolder.DAMAGER;
    }
}
