package com.thebluehats.server.game.enchants;

import java.util.ArrayList;
import java.util.List;

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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Shark extends DamageTriggeredEnchant {
    private final EnchantProperty<Float> percentDamageIncrease = new EnchantProperty<>(0.02f, 0.04f, 0.07f);

    private final DamageManager damageManager;

    @Inject
    public Shark(DamageManager damageManager, PlayerDamageTrigger playerDamageTrigger) {
        super(new DamageEnchantTrigger[] { playerDamageTrigger }, new EntityValidator[] { damageManager });

        this.damageManager = damageManager;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        EntityDamageByEntityEvent event = data.getEvent();
        Player damager = data.getDamager();
        int level = data.getLevel();

        List<Entity> entities = damager.getNearbyEntities(12, 12, 12);
        List<Player> players = new ArrayList<>();

        for (Entity entity : entities) {
            if (entity instanceof Player) {
                if (entity != damager) {
                    players.add((Player) entity);
                }
            }
        }

        damageManager.addDamage(event, percentDamageIncrease.getValueAtLevel(level) * players.size(),
                CalculationMode.ADDITIVE);
    }

    @Override
    public String getName() {
        return "Shark";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Shark";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("");

        enchantLoreParser.setSingleVariable("", "", "");

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
