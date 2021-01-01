package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.templates.*;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LastStand extends DamageTriggeredEnchant {
    private final EnchantProperty<Integer> resistanceAmplifier = new EnchantProperty<>(0, 1, 2);

    @Inject
    public LastStand(PlayerDamageTrigger playerDamageTrigger, ArrowDamageTrigger arrowDamageTrigger) {
        super(new DamageEnchantTrigger[] { playerDamageTrigger, arrowDamageTrigger });
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        Player damagee = data.getDamagee();

        if (damagee.getHealth() < 10)
            damagee.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80,
                    resistanceAmplifier.getValueAtLevel(data.getLevel())), true);
    }

    @Override
    public String getName() {
        return "Last Stand";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Laststand";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Gain <blue>Resistance {0}</blue> (4<br/>seconds) when reaching <red>3❤</red>");

        enchantLoreParser.setSingleVariable("I", "II", "III");

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
        return false;
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