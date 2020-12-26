package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.processedevents.PostDamageEventTemplateResult;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.TargetPlayer;
import com.thebluehats.server.game.managers.enchants.DamageEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.HitCounter;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ComboSwift implements DamageEnchant {
    private final EnchantProperty<Integer> speedTime = new EnchantProperty<>(3, 4, 5);
    private final EnchantProperty<Integer> speedAmplifier = new EnchantProperty<>(0, 1, 1);
    private final EnchantProperty<Integer> hitsNeeded = new EnchantProperty<>(4, 3, 3);

    private final HitCounter hitCounter;
    private final PlayerHitPlayerTemplate playerHitPlayerTemplate;

    @Inject
    public ComboSwift(HitCounter hitCounter, PlayerHitPlayerTemplate playerHitPlayerTemplate) {
        this.hitCounter = hitCounter;
        this.playerHitPlayerTemplate = playerHitPlayerTemplate;
    }

    @Override
    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        playerHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGER);
    }

    @Override
    public void execute(PostDamageEventTemplateResult data) {
        Player damager = data.getDamager();
        int level = data.getPrimaryLevel();

        hitCounter.addOne(damager);

        if (hitCounter.hasHits(damager, hitsNeeded.getValueAtLevel(level))) {
            damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, speedTime.getValueAtLevel(level) * 20,
                    speedAmplifier.getValueAtLevel(level), true));
        }
    }

    @Override
    public String getName() {
        return "Combo: Swift";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Comboswift";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "<Every <yellow>{0}</yellow> strike gain<br/><yellow>Speed {1}</yellow> ({2}s)");

        String[][] variables = new String[3][];
        variables[0] = new String[] { "fourth", "third", "third" };
        variables[1] = new String[] { "I", "II", "III" };
        variables[2] = new String[] { "3", "4", "5" };

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
        return new Material[] { Material.GOLDEN_SWORD };
    }
}
