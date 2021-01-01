package com.thebluehats.server.game.enchants;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.templates.DamageEventVerificationTemplate;
import com.thebluehats.server.game.managers.enchants.processedevents.CastedEntityDamageByEntityEvent;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerificationTemplate;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerificationTemplate;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import com.thebluehats.server.game.utils.EntityValidator;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Solitude extends DamageTriggeredEnchant {
    private final EnchantProperty<Float> damageReduction = new EnchantProperty<>(.4f, .5f, .6f);
    private final EnchantProperty<Integer> playersNeeded = new EnchantProperty<>(1, 2, 2);

    private final DamageManager damageManager;

    @Inject
    public Solitude(DamageManager damageManager, PlayerHitPlayerVerificationTemplate playerHitPlayerTemplate,
            ArrowHitPlayerVerificationTemplate arrowHitPlayerTemplate) {
        super(new DamageEventVerificationTemplate[] { playerHitPlayerTemplate, arrowHitPlayerTemplate }, new EntityValidator[] { damageManager });

        this.damageManager = damageManager;
    }

    @Override
    public void execute(CastedEntityDamageByEntityEvent data) {
        EntityDamageByEntityEvent event = data.getEvent();
        Player damagee = data.getDamagee();
        int level = data.getLevel();

        List<Entity> entities = damagee.getNearbyEntities(7, 7, 7);
        List<Player> players = new ArrayList<>();

        for (Entity entity : entities) {
            if (entity instanceof Player) {
                if (entity != damagee) {
                    players.add((Player) entity);
                }
            }
        }

        if (players.size() <= playersNeeded.getValueAtLevel(level)) {
            damageManager.reduceDamageByPercentage(event, damageReduction.getValueAtLevel(level));
        }
    }

    @Override
    public String getName() {
        return "Solitude";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Solitude";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Recieve <blue>-{0}</blue> damage when only<br/>one other player is within 7<br/>blocks");

        enchantLoreParser.setSingleVariable("40%", "50%", "60%");

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
        return true;
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
