package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerificationTemplate;
import com.thebluehats.server.game.managers.combat.templates.DamageEventVerificationTemplate;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.processedevents.CastedEntityDamageByEntityEvent;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import com.thebluehats.server.game.utils.EntityValidator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Punisher extends DamageTriggeredEnchant {
    private final EnchantProperty<Float> percentDamageIncrease = new EnchantProperty<>(.6f, .12f, .18f);

    private final DamageManager damageManager;

    @Inject
    public Punisher(DamageManager damageManager, PlayerHitPlayerVerificationTemplate playerHitPlayerTemplate) {
        super(new DamageEventVerificationTemplate[] { playerHitPlayerTemplate }, new EntityValidator[] { damageManager });

        this.damageManager = damageManager;
    }

    @Override
    public void execute(CastedEntityDamageByEntityEvent data) {
        EntityDamageByEntityEvent event = data.getEvent();
        Player damagee = data.getDamagee();
        int level = data.getLevel();

        if (damagee.getHealth() < damagee.getMaxHealth() / 2) {
            damageManager.addDamage(event, percentDamageIncrease.getValueAtLevel(level), CalculationMode.ADDITIVE);
        }
    }

    @Override
    public String getName() {
        return "Punisher";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Punisher";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Do <red>+{0}</red> damage vs. players<br/>below 50% HP");

        enchantLoreParser.setSingleVariable("7%", "12%", "18%");

        return enchantLoreParser.parseForLevel(level);
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
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
