package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger;
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BottomlessQuiver extends DamageTriggeredEnchant {
    private final EnchantProperty<Integer> arrowAmount = new EnchantProperty<>(1, 3, 8);

    @Inject
    public BottomlessQuiver(ArrowDamageTrigger arrowDamageTrigger) {
        super(new DamageEnchantTrigger[] { arrowDamageTrigger });
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        Player damager = data.getDamager();
        int level = data.getLevel();
        ItemStack arrows = new ItemStack(Material.ARROW, arrowAmount.getValueAtLevel(level));

        damager.getInventory().addItem(arrows);
    }

    @Override
    public String getName() {
        return "Bottomless Quiver";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Bottomlessquiver";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("");

        enchantLoreParser.addTextIf(level == 1, "Get <white>{0} arrow</white> on arrow hit");
        enchantLoreParser.addTextIf(level != 1, "Get <white>{0} arrows</white> on arrow hit");

        enchantLoreParser.setSingleVariable("1", "3", "8");

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
        return new Material[] { Material.BOW };
    }

    @Override
    public EnchantHolder getEnchantHolder() {
        return EnchantHolder.DAMAGER;
    }
}
