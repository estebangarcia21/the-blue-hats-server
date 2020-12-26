package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.combat.templates.PostDamageEventTemplate;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.OnDamageEnchant;
import com.thebluehats.server.game.managers.enchants.processedevents.PostDamageEventResult;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SprintDrain extends OnDamageEnchant {
    private final EnchantProperty<Integer> speedDuration = new EnchantProperty<>(5, 5, 7);
    private final EnchantProperty<Integer> speedAmplifier = new EnchantProperty<>(0, 0, 1);

    @Inject
    public SprintDrain(ArrowHitPlayerTemplate arrowHitPlayerTemplate) {
        super(new PostDamageEventTemplate[] { arrowHitPlayerTemplate });
    }

    @Override
    public void execute(PostDamageEventResult data) {
        int level = data.getLevel();

        data.getDamager().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                speedDuration.getValueAtLevel(level) * 20, speedAmplifier.getValueAtLevel(level)));

        if (level == 1)
            return;

        data.getDamagee().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 0));
    }

    @Override
    public String getName() {
        return "Sprint Drain";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Sprintdrain";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Arrow shots grant you <yellow>Speed {0}</yellow><br/>({1}s)");

        enchantLoreParser.addTextIf(level != 1, " and apply <blue>Slowness I</blue><br/>(3s)");

        String[][] variables = new String[2][];
        variables[0] = new String[] {};
        variables[1] = new String[] {};

        enchantLoreParser.setVariables(variables);

        return enchantLoreParser.parseForLevel(level);
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.C;
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