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
import org.bukkit.entity.Player;

public class Chipping extends DamageTriggeredEnchant {
    private final EnchantProperty<Float> damageAmount = new EnchantProperty<>(0.5f, 1.0f, 1.5f);

    private final DamageManager damageManager;

    @Inject
    public Chipping(DamageManager damageManager, ArrowDamageTrigger arrowDamageTrigger) {
        super(new DamageEnchantTrigger[] { arrowDamageTrigger }, new EntityValidator[] { damageManager });

        this.damageManager = damageManager;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        Player damaged = data.getDamagee();
        int level = data.getLevel();

        damageManager.doTrueDamage(damaged, damageAmount.getValueAtLevel(level));
    }

    @Override
    public String getName() {
        return "Chipping";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Chipping";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("Deals <red>{0}</red> extra true damage");

        enchantLoreParser.setSingleVariable("0.5❤ ", "1.0❤ ", "1.5❤ ");

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
