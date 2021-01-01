package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.managers.combat.BowManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerificationTemplate;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.combat.templates.DamageEventVerificationTemplate;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.processedevents.CastedEntityDamageByEntityEvent;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import java.util.ArrayList;

public class Wasp extends DamageTriggeredEnchant {
    private final EnchantProperty<Integer> weaknessDuration = new EnchantProperty<>(6, 11, 16);
    private final EnchantProperty<Integer> weaknessAmplifier = new EnchantProperty<>(1, 2, 3);

    private final BowManager bowManager;

    @Inject
    public Wasp(BowManager bowManager, ArrowHitPlayerVerificationTemplate arrowHitPlayerTemplate) {
        super(new DamageEventVerificationTemplate[] { arrowHitPlayerTemplate });

        this.bowManager = bowManager;
    }

    @Override
    public void execute(CastedEntityDamageByEntityEvent data) {
        int level = data.getLevel();

        data.getDamager().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
                weaknessDuration.getValueAtLevel(level) * 20, weaknessAmplifier.getValueAtLevel(level)), true);
    }

    @EventHandler
    public void onArrowShootEvent(EntityShootBowEvent event) {
        bowManager.onArrowShoot(event);
    }

    @Override
    public String getName() {
        return "Wasp";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Wasp";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("Apply <red>Weakness {0}</red> ({1}s) on hit");

        String[][] variables = new String[2][];
        variables[0] = new String[] { "II", "III", "IV" };
        variables[1] = new String[] { "6", "11", "16" };

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
        return new Material[] { Material.BOW };
    }

    @Override
    public EnchantHolder getEnchantHolder() {
        return EnchantHolder.DAMAGER;
    }
}
