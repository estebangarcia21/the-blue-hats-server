package com.thebluehats.server.game.enchants;

import com.google.inject.Inject;
import com.thebluehats.server.api.daos.PitDataDao;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.*;
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

public class FractionalReserve extends DamageTriggeredEnchant {
    private final EnchantProperty<Double> maximumDamageReduction = new EnchantProperty<>(.15D, .21D, .30D);

    private final PitDataDao pitData;
    private final DamageManager damageManager;

    @Inject
    public FractionalReserve(DamageManager damageManager, PlayerDamageTrigger playerDamageTrigger,
                             ArrowDamageTrigger arrowDamageTrigger, PitDataDao pitData) {
        super(new DamageEnchantTrigger[] { playerDamageTrigger, arrowDamageTrigger },
                new EntityValidator[] { damageManager });

        this.damageManager = damageManager;
        this.pitData = pitData;
    }

    @Override
    public String getName() {
        return "Fractional Reserve";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Frac";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Recieve <blue>-1% damage</blue> per<br/><gold>10,000g</gold> you have (<blue>-{0}</blue><br/>max");

        enchantLoreParser.setSingleVariable("15%", "21%", "30%");
        
        return enchantLoreParser.parseForLevel(level);
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        // TODO Group needs to be changed to AUCTION
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
    public void execute(DamageEventEnchantData data) {
        EntityDamageByEntityEvent event = data.getEvent();
        Player damagee = data.getDamagee();
        int level = data.getLevel();

        double damageReduction = 0D;

        for (int i = 10000; i <= pitData.getPlayerGold(damagee); i += 10000) {
            damageReduction += .10d;
        }

        if (damageReduction > maximumDamageReduction.getValueAtLevel(level)) {
            damageReduction = maximumDamageReduction.getValueAtLevel(level);
        }

        damageManager.reduceDamageByPercentage(event, maximumDamageReduction.getValueAtLevel(level));
    }

    @Override
    public EnchantHolder getEnchantHolder() {
        return EnchantHolder.DAMAGEE;
    }
}
