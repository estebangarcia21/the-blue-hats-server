package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.processedevents.PostEventTemplateResult;
import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.TargetPlayer;
import com.thebluehats.server.game.managers.enchants.DamageEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.api.daos.PitDataDao;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Billionaire implements DamageEnchant {
    private final EnchantProperty<Double> damageIncrease = new EnchantProperty<>(1.33D, 1.67D, 2D);
    private final EnchantProperty<Integer> goldNeeded = new EnchantProperty<>(100, 200, 350);

    private final PitDataDao pitData;
    private final DamageManager damageManager;
    private final PlayerHitPlayerTemplate playerHitPlayerTemplate;

    @Inject
    public Billionaire(PitDataDao pitData, DamageManager damageManager,
                       PlayerHitPlayerTemplate playerHitPlayerTemplate) {
        this.pitData = pitData;
        this.damageManager = damageManager;
        this.playerHitPlayerTemplate = playerHitPlayerTemplate;
    }

    @Override
    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        playerHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGER, damageManager);
    }

    @Override
    public void execute(PostEventTemplateResult data) {
        Player damager = data.getDamager();
        int level = data.getPrimaryLevel();

        double gold = pitData.getPlayerGold(damager);

        if (gold < goldNeeded.getValueAtLevel(level))
            return;

        pitData.setPlayerGold(damager, gold - goldNeeded.getValueAtLevel(level));

        damageManager.addDamage(data.getEvent(), damageIncrease.getValueAtLevel(level), CalculationMode.MULTIPLICATIVE);

        damager.playSound(damager.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.73f);
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
        return new Material[] { Material.GOLDEN_SWORD };
    }
}