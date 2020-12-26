package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.processedevents.PostDamageEventTemplateResult;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.TargetPlayer;
import com.thebluehats.server.game.managers.enchants.OnDamageEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Peroxide implements OnDamageEnchant {
    private final EnchantProperty<Integer> regenDuration = new EnchantProperty<>(5, 8, 8);
    private final EnchantProperty<Integer> regenAmplifier = new EnchantProperty<>(0, 0, 1);

    private final ArrowHitPlayerTemplate arrowHitPlayerTemplate;
    private final PlayerHitPlayerTemplate playerHitPlayerTemplate;

    @Inject
    public Peroxide(PlayerHitPlayerTemplate playerHitPlayerTemplate, ArrowHitPlayerTemplate arrowHitPlayerTemplate) {
        this.playerHitPlayerTemplate = playerHitPlayerTemplate;
        this.arrowHitPlayerTemplate = arrowHitPlayerTemplate;
    }

    @Override
    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        playerHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGEE);
        arrowHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGEE);
    }

    @Override
    public void execute(PostDamageEventTemplateResult data) {
        int level = data.getPrimaryLevel();

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
}