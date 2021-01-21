package com.thebluehats.server.game.enchants;

import java.util.ArrayList;
import java.util.Random;

import com.google.inject.Inject;
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
import org.bukkit.Sound;

import org.bukkit.entity.Player;

public class Gamble extends DamageTriggeredEnchant {
    private final EnchantProperty<Integer> damageAmount = new EnchantProperty<>(2, 4, 6);
    private final Random random = new Random();

    private final DamageManager damageManager;

    @Inject
    public Gamble(DamageManager damageManager, PlayerDamageTrigger playerDamageTrigger) {
        super(new DamageEnchantTrigger[] { playerDamageTrigger }, new EntityValidator[] { damageManager });

        this.damageManager = damageManager;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        Player damager = data.getDamager();
        Player damagee = data.getDamagee();
        int level = data.getLevel();
        int damage = damageAmount.getValueAtLevel(level);

        float chance = 0.5f;
        float randomValue = random.nextFloat();

        if (randomValue >= chance) {
            damageManager.doTrueDamage(damagee, damage, damager);

            damager.playSound(damager.getLocation(), Sound.NOTE_PLING, 1, 3f);
        } else {
            damageManager.doTrueDamage(damager, damage, damagee);

            damagee.playSound(damager.getLocation(), Sound.NOTE_PLING, 1, 1.5f);
        }
    }

    @Override
    public String getName() {
        return "Gamble";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Gamble";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "<light_purple>50% chance</light_purple> to deal <red>{0}</red> true<br/>damage to whoever you hit, or to<br/>yourself");

        enchantLoreParser.setSingleVariable("1❤", "2❤", "3❤");

        return enchantLoreParser.parseForLevel(level);
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        // TODO Determine EnchantGroup
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
