package com.thebluehats.server.game.enchants;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.PostDamageEventTemplate;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.HitCounter;
import com.thebluehats.server.game.managers.enchants.OnDamageEnchant;
import com.thebluehats.server.game.managers.enchants.processedevents.PostDamageEventResult;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class ComboSwift extends OnDamageEnchant {
    private final EnchantProperty<Integer> speedTime = new EnchantProperty<>(3, 4, 5);
    private final EnchantProperty<Integer> speedAmplifier = new EnchantProperty<>(0, 1, 1);
    private final EnchantProperty<Integer> hitsNeeded = new EnchantProperty<>(4, 3, 3);

    private final HitCounter hitCounter;

    @Inject
    public ComboSwift(HitCounter hitCounter, PlayerHitPlayerTemplate playerHitPlayerTemplate) {
        super(new PostDamageEventTemplate[] { playerHitPlayerTemplate });

        this.hitCounter = hitCounter;
    }

    @Override
    public void execute(PostDamageEventResult data) {
        Player damager = data.getDamager();
        int level = data.getLevel();

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
        return new Material[] { Material.GOLD_SWORD };
    }

    @Override
    public EnchantHolder getEnchantHolder() {
        return EnchantHolder.DAMAGER;
    }
}
