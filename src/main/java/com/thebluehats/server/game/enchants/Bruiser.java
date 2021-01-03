package com.thebluehats.server.game.enchants;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger;
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

import java.util.ArrayList;

public class Bruiser extends DamageTriggeredEnchant {
    private final EnchantProperty<Integer> heartsReduced = new EnchantProperty<>(1, 2, 4);

    private final DamageManager damageManager;

    @Inject
    public Bruiser(DamageManager damageManager, PlayerDamageTrigger playerDamageTrigger, ArrowDamageTrigger arrowDamageTrigger) {
        super(new DamageEnchantTrigger[] { playerDamageTrigger, arrowDamageTrigger }, new EntityValidator[] { damageManager });

        this.damageManager = damageManager;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        damageManager.addHeartDamageReduction(data.getEvent(), heartsReduced.getValueAtLevel(data.getLevel()));
    }

    @Override
    public String getName() {
        return "Bruiser";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Bruiser";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("Blocking with your sword reduces<br/>damage received by <red>{0}</red>");

        enchantLoreParser.setSingleVariable("0.5❤", "1❤", "2❤");

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
        return new Material[] { Material.GOLD_SWORD };
    }

    @Override
    public EnchantHolder getEnchantHolder() {
        return EnchantHolder.DAMAGEE;
    }
}
