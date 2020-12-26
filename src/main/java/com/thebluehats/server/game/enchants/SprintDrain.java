package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.processedevents.PostEventTemplateResult;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.TargetPlayer;
import com.thebluehats.server.game.managers.enchants.DamageEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SprintDrain implements DamageEnchant {
    private final EnchantProperty<Integer> speedDuration = new EnchantProperty<>(5, 5, 7);
    private final EnchantProperty<Integer> speedAmplifier = new EnchantProperty<>(0, 0, 1);

    private final ArrowHitPlayerTemplate arrowHitPlayerTemplate;

    @Inject
    public SprintDrain(ArrowHitPlayerTemplate arrowHitPlayerTemplate) {
        this.arrowHitPlayerTemplate = arrowHitPlayerTemplate;
    }

    @Override
    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        arrowHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGER);
    }

    @Override
    public void execute(PostEventTemplateResult data) {
        int level = data.getPrimaryLevel();

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
}