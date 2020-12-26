package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.combat.templates.PostDamageEventTemplate;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.OnDamageEnchant;
import com.thebluehats.server.game.managers.enchants.processedevents.PostDamageEventResult;
import com.thebluehats.server.game.utils.EntityValidator;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Fletching extends OnDamageEnchant {
    private final EnchantProperty<Float> percentDamageIncrease = new EnchantProperty<>(.07f, 0.12f, 0.20f);

    private final DamageManager damageManager;

    @Inject
    public Fletching(DamageManager damageManager, ArrowHitPlayerTemplate arrowHitPlayerTemplate) {
        super(new PostDamageEventTemplate[] { arrowHitPlayerTemplate }, new EntityValidator[] { damageManager });

        this.damageManager = damageManager;
    }

    @Override
    public void execute(PostDamageEventResult data) {
        EntityDamageByEntityEvent event = data.getEvent();
        int level = data.getLevel();

        damageManager.addDamage(event, percentDamageIncrease.getValueAtLevel(level), CalculationMode.ADDITIVE);
    }

    @Override
    public String getName() {
        return "Fletching";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Fletching";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return null;
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
        return new Material[] { Material.BOW };
    }

    @Override
    public EnchantHolder getEnchantHolder() {
        return EnchantHolder.DAMAGER;
    }
}
