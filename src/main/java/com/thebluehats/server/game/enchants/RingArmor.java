package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger;
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import com.thebluehats.server.game.utils.EntityValidator;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class RingArmor extends DamageTriggeredEnchant {
    private final EnchantProperty<Float> damageReductionAmount = new EnchantProperty<>(.20f, .40f, .60f);

    private final DamageManager damageManager;

    @Inject
    public RingArmor(DamageManager damageManager, ArrowDamageTrigger arrowDamageTrigger) {
        super(new DamageEnchantTrigger[] { arrowDamageTrigger }, new EntityValidator[] { damageManager });

        this.damageManager = damageManager;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        EntityDamageByEntityEvent event = data.getEvent();
        int level = data.getLevel();

        damageManager.reduceDamageByPercentage(event, damageReductionAmount.getValueAtLevel(level));
    }

    @Override
    public String getName() {
        return "Ring Armor";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Ringarmor";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Recieve <blue>-{0}%</blue> damage from<br/>arrows");

        enchantLoreParser.setSingleVariable("20", "40", "60");

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
        return new Material[] { Material.LEATHER_LEGGINGS };
    }

    @Override
    public EnchantHolder getEnchantHolder() {
        return EnchantHolder.DAMAGEE;
    }
}