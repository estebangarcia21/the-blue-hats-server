package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.enchants.processedevents.PostEventTemplateResult;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.TargetPlayer;
import com.thebluehats.server.game.managers.enchants.DamageEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Chipping implements DamageEnchant {
    private final EnchantProperty<Float> damageAmount = new EnchantProperty<>(0.5f, 1.0f, 1.5f);

    private final DamageManager damageManager;
    private final ArrowHitPlayerTemplate arrowHitPlayerTemplate;

    @Inject
    public Chipping(DamageManager damageManager, ArrowHitPlayerTemplate arrowHitPlayerTemplate) {
        this.damageManager = damageManager;
        this.arrowHitPlayerTemplate = arrowHitPlayerTemplate;
    }

    @Override
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        arrowHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGER);
    }

    @Override
    public void execute(PostEventTemplateResult data) {
        Player damaged = data.getDamagee();
        int level = data.getPrimaryLevel();

        damageManager.doTrueDamage(damaged, damageAmount.getValueAtLevel(level));
    }

    @Override
    public String getName() {
        return "Chipping";
    }

    @Override
    public String getEnchantReferenceName() {
        return "chipping";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("Deals <red>{0}</red> extra true damage");

        enchantLoreParser.setSingleVariable("0.5❤ ", "1.0❤ ", "1.5❤ ");

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
        return new Material[] { Material.BOW };
    }
}
