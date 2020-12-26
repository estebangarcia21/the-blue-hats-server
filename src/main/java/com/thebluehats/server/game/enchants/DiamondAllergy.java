package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.processedevents.PostDamageEventTemplateResult;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.TargetPlayer;
import com.thebluehats.server.game.managers.enchants.OnDamageEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DiamondAllergy implements OnDamageEnchant {
    private final EnchantProperty<Float> damageReduction = new EnchantProperty<>(0.10f, 0.20f, 0.30f);

    private final DamageManager damageManager;
    private final PlayerHitPlayerTemplate playerHitPlayerTemplate;

    @Inject
    public DiamondAllergy(DamageManager damageManager, PlayerHitPlayerTemplate playerHitPlayerTemplate) {
        this.damageManager = damageManager;
        this.playerHitPlayerTemplate = playerHitPlayerTemplate;
    }

    @Override
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        playerHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGEE);
    }

    @Override
    public void execute(PostDamageEventTemplateResult data) {
        EntityDamageByEntityEvent event = data.getEvent();
        Player damager = data.getDamager();
        int level = data.getPrimaryLevel();

        if (damager.getInventory().getItemInMainHand().getType() == Material.DIAMOND_SWORD) {
            damageManager.reduceDamageByPercentage(event, damageReduction.getValueAtLevel(level));
        }
    }

    @Override
    public String getName() {
        return "Diamond Allergy";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Diamondallergy";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Recieve <blue>-{0}</blue> damage from<br/>diamond weapons");

        enchantLoreParser.setSingleVariable("10%", "20%", "30%");

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
        return new Material[] { Material.LEATHER_LEGGINGS };
    }
}
