package com.thebluehats.server.game.enchants;

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

import java.util.ArrayList;

public class PainFocus extends DamageTriggeredEnchant {
    private final EnchantProperty<Float> damageIncreasePerHeartLost = new EnchantProperty<>(0.01f, 0.02f, 0.05f);

    private final DamageManager damageManager;

    @Inject
    public PainFocus(DamageManager damageManager, PlayerDamageTrigger playerDamageTrigger) {
        super(new DamageEnchantTrigger[] { playerDamageTrigger }, new EntityValidator[] { damageManager });

        this.damageManager = damageManager;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        EntityDamageByEntityEvent event = data.getEvent();
        Player damager = data.getDamager();
        int level = data.getLevel();

        int heartsLost = (int) (damager.getMaxHealth() - damager.getHealth()) / 2;

        damageManager.addDamage(event, damageIncreasePerHeartLost.getValueAtLevel(level) * heartsLost,
                CalculationMode.ADDITIVE);
    }

    @Override
    public String getName() {
        return "Pain Focus";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Painfocus";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Deal <red>+{0}</red> damage per <red>❤</red><br/>you're missing");

        enchantLoreParser.setSingleVariable("1%", "+2%", "5%");

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