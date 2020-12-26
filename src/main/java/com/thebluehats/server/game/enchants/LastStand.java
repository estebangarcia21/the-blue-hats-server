package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.processedevents.PostDamageEventTemplateResult;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.TargetPlayer;
import com.thebluehats.server.game.managers.enchants.OnDamageEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LastStand implements OnDamageEnchant {
    private final EnchantProperty<Integer> resistanceAmplifier = new EnchantProperty<>(0, 1, 2);

    private final ArrowHitPlayerTemplate arrowHitPlayerTemplate;
    private final PlayerHitPlayerTemplate playerHitPlayerTemplate;

    @Inject
    public LastStand(PlayerHitPlayerTemplate playerHitPlayerTemplate, ArrowHitPlayerTemplate arrowHitPlayerTemplate) {
        this.playerHitPlayerTemplate = playerHitPlayerTemplate;
        this.arrowHitPlayerTemplate = arrowHitPlayerTemplate;
    }

    @Override
    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        playerHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGEE);
        arrowHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGEE);
    }

    @Override
    public void execute(PostDamageEventTemplateResult data) {
        Player damagee = data.getDamagee();

        if (damagee.getHealth() < 10)
            damagee.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80,
                    resistanceAmplifier.getValueAtLevel(data.getPrimaryLevel()), true));
    }

    @Override
    public String getName() {
        return "Last Stand";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Laststand";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Gain <blue>Resistance {0}</blue> (4<br/>seconds) when reaching 3❤");

        enchantLoreParser.setSingleVariable("I", "II", "III");

        return enchantLoreParser.parseForLevel(level);
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return true;
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
        return new Material[] { Material.LEATHER_LEGGINGS };
    }
}