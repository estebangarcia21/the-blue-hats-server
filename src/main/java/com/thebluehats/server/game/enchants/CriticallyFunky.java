package com.thebluehats.server.game.enchants;

import java.util.ArrayList;
import java.util.UUID;

import com.google.inject.Inject;
import com.thebluehats.server.game.enchants.processedevents.PostEventTemplateResult;
import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.TargetPlayer;
import com.thebluehats.server.game.managers.enchants.DamageEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.LoreBuilder;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;

public class CriticallyFunky implements DamageEnchant {
    private final EnchantProperty<Float> damageReduction = new EnchantProperty<>(0.35f, 0.35f, 0.6f);
    private final EnchantProperty<Float> damageIncrease = new EnchantProperty<>(0f, .14f, .3f);
    private final ArrayList<UUID> extraDamageQueue = new ArrayList<>();

    private final DamageManager damageManager;
    private final PlayerHitPlayerTemplate playerHitPlayerTemplate;
    private final ArrowHitPlayerTemplate arrowHitPlayerTemplate;

    @Inject
    public CriticallyFunky(DamageManager damageManager, PlayerHitPlayerTemplate playerHitPlayerTemplate,
            ArrowHitPlayerTemplate arrowHitPlayerTemplate) {
        this.damageManager = damageManager;
        this.playerHitPlayerTemplate = playerHitPlayerTemplate;
        this.arrowHitPlayerTemplate = arrowHitPlayerTemplate;
    }

    @Override
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        playerHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGER, PlayerInventory::getItemInMainHand,
                damageManager);
        arrowHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGER, PlayerInventory::getItemInMainHand,
                damageManager);
    }

    @Override
    public void execute(PostEventTemplateResult data, int level) {
        EntityDamageByEntityEvent event = data.getEvent();

        Player damager = data.getDamager();

        if (!damageManager.isCriticalHit(damager))
            return;

        if (extraDamageQueue.contains(damager.getUniqueId())) {
            damageManager.addDamage(event, damageIncrease.getValueAtLevel(level), CalculationMode.ADDITIVE);
            extraDamageQueue.remove(damager.getUniqueId());
        }

        if (level != 1) {
            extraDamageQueue.add(event.getEntity().getUniqueId());
        }

        damageManager.reduceDamage(event, damageReduction.getValueAtLevel(level));
        damageManager.removeExtraCriticalDamage(event);
    }

    @Override
    public String getName() {
        return "Critically Funky";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Critfunky";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("65%", "65%", "40%").declareVariable("", "14%", "30%")
                .write("Critical hits against you deal").next().setColor(ChatColor.BLUE).writeVariable(0, level)
                .resetColor().write(" of the damage they").next().write("normally would").setWriteCondition(level != 1)
                .write(" and empower your").next().write("next strike for ").setColor(ChatColor.RED)
                .writeVariable(1, level).resetColor().write(" damage").build();
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
        return new Material[] { Material.LEATHER_LEGGINGS };
    }
}
