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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Peroxide extends DamageTriggeredEnchant {
    private final EnchantProperty<Integer> regenDuration = new EnchantProperty<>(5, 8, 8);
    private final EnchantProperty<Integer> regenAmplifier = new EnchantProperty<>(0, 0, 1);

    @Inject
    public Peroxide(PlayerDamageTrigger playerDamageTrigger, ArrowDamageTrigger arrowDamageTrigger) {
        super(new DamageEnchantTrigger[] { playerDamageTrigger, arrowDamageTrigger });
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        int level = data.getLevel();

        data.getDamagee().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                regenDuration.getValueAtLevel(level) * 20, regenAmplifier.getValueAtLevel(level), true));
    }

    @Override
    public String getName() {
        return "Peroxide";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Peroxide";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("Gain <red>Regen {0}</red> ({1}s) when hit");

        String[][] variables = new String[2][];
        variables[0] = new String[] { "I", "I", "II" };
        variables[1] = new String[] { "5", "8", "8" };

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