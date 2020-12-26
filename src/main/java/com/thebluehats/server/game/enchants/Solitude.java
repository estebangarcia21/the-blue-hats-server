package com.thebluehats.server.game.enchants;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.processedevents.PostEventTemplateResult;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.TargetPlayer;
import com.thebluehats.server.game.managers.enchants.DamageEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Solitude implements DamageEnchant {
    private final EnchantProperty<Float> damageReduction = new EnchantProperty<>(.4f, .5f, .6f);
    private final EnchantProperty<Integer> playersNeeded = new EnchantProperty<>(1, 2, 2);

    private final DamageManager damageManager;

    private final PlayerHitPlayerTemplate playerHitPlayerTemplate;
    private final ArrowHitPlayerTemplate arrowHitPlayerTemplate;

    @Inject
    public Solitude(DamageManager damageManager, PlayerHitPlayerTemplate playerHitPlayerTemplate,
            ArrowHitPlayerTemplate arrowHitPlayerTemplate) {
        this.damageManager = damageManager;
        this.playerHitPlayerTemplate = playerHitPlayerTemplate;
        this.arrowHitPlayerTemplate = arrowHitPlayerTemplate;
    }

    @Override
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        playerHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGEE);
        arrowHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGEE);
    }

    @Override
    public void execute(PostEventTemplateResult data) {
        EntityDamageByEntityEvent event = data.getEvent();
        Player damagee = data.getDamagee();
        int level = data.getPrimaryLevel();

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
}
