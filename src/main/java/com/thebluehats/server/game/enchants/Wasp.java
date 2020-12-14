package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import javax.inject.Inject;

import com.thebluehats.server.game.enchants.processedevents.PostEventTemplateResult;
import com.thebluehats.server.game.managers.combat.BowManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.TargetPlayer;
import com.thebluehats.server.game.managers.enchants.DamageEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.LoreBuilder;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Wasp implements DamageEnchant {
    private final EnchantProperty<Integer> weaknessDuration = new EnchantProperty<>(6, 11, 16);
    private final EnchantProperty<Integer> weaknessAmplifier = new EnchantProperty<>(1, 2, 3);

    private final BowManager bowManager;
    private final ArrowHitPlayerTemplate arrowHitPlayerTemplate;

    @Inject
    public Wasp(BowManager bowManager, ArrowHitPlayerTemplate arrowHitPlayerTemplate) {
        this.bowManager = bowManager;
        this.arrowHitPlayerTemplate = arrowHitPlayerTemplate;
    }

    @Override
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        arrowHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGER, PlayerInventory::getItemInMainHand);
    }

    @Override
    public void execute(PostEventTemplateResult data, int level) {
        data.getDamager().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
                weaknessDuration.getValueAtLevel(level) * 20, weaknessAmplifier.getValueAtLevel(level)));
    }

    @EventHandler
    public void onArrowShootEvent(EntityShootBowEvent event) {
        bowManager.onArrowShoot(event);
    }

    @Override
    public String getName() {
        return "Wasp";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Wasp";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("II", "III", "IV").setColor(ChatColor.GRAY).write("Apply ")
                .setColor(ChatColor.RED).write("Weakness ").writeVariable(0, level).setColor(ChatColor.GRAY)
                .write(" (" + weaknessDuration.getValueAtLevel(level) + "s) on hit").build();
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
