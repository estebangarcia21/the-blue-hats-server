package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import com.thebluehats.server.game.utils.EntityValidator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Sharp extends DamageTriggeredEnchant {
    private final EnchantProperty<Float> percentDamageIncrease = new EnchantProperty<>(0.04f, 0.07f, 0.12f);

    private final DamageManager damageManager;

    @Inject
    public Sharp(DamageManager damageManager, PlayerDamageTrigger playerDamageTrigger) {
        super(new DamageEnchantTrigger[] { playerDamageTrigger }, new EntityValidator[] { damageManager });

        this.damageManager = damageManager;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        EntityDamageByEntityEvent event = data.getEvent();
        int level = data.getLevel();

        damageManager.addDamage(event, percentDamageIncrease.getValueAtLevel(level), CalculationMode.ADDITIVE);
    }

    @Override
    public String getName() {
        return "Sharp";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Sharp";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("Deal <red>+{0}</red> melee damage");

        enchantLoreParser.setSingleVariable("4%", "7%", "12%");

        return enchantLoreParser.parseForLevel(level);
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        // TODO Determine EnchantGroup
        return EnchantGroup.A;
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
