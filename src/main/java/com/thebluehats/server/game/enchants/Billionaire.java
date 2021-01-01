package com.thebluehats.server.game.enchants;

import com.google.inject.Inject;
import com.thebluehats.server.api.daos.PitDataDao;
import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerificationTemplate;
import com.thebluehats.server.game.managers.combat.templates.DamageEventVerificationTemplate;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.processedevents.CastedEntityDamageByEntityEvent;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Billionaire extends DamageTriggeredEnchant {
    private final EnchantProperty<Double> damageIncrease = new EnchantProperty<>(1.33D, 1.67D, 2D);
    private final EnchantProperty<Integer> goldNeeded = new EnchantProperty<>(100, 200, 350);

    private final PitDataDao pitData;
    private final DamageManager damageManager;

    @Inject
    public Billionaire(PitDataDao pitData, DamageManager damageManager,
            PlayerHitPlayerVerificationTemplate playerHitPlayerTemplate) {
        super(new DamageEventVerificationTemplate[] { playerHitPlayerTemplate });

        this.pitData = pitData;
        this.damageManager = damageManager;
    }

    @Override
    public void execute(CastedEntityDamageByEntityEvent data) {
        Player damager = data.getDamager();
        int level = data.getLevel();

        double gold = pitData.getPlayerGold(damager);

        if (gold < goldNeeded.getValueAtLevel(level))
            return;

        pitData.setPlayerGold(damager, gold - goldNeeded.getValueAtLevel(level));

        damageManager.addDamage(data.getEvent(), damageIncrease.getValueAtLevel(level), CalculationMode.MULTIPLICATIVE);

        damager.playSound(damager.getLocation(), Sound.ORB_PICKUP, 1, 0.73f);
    }

    @Override
    public String getName() {
        return "Billionaire";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Billionaire";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Hits with this swords deals <red>{0}x</red><br/>damage but cost <gold>{1}</gold>");

        String[][] variables = new String[2][];
        variables[0] = new String[] { "1.33", "1.67", "2" };
        variables[1] = new String[] { "100g", "200g", "350g" };

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