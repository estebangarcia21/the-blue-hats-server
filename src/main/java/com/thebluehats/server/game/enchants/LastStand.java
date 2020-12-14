package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.enchants.processedevents.PostEventTemplateResult;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LastStand implements DamageEnchant {
    private final EnchantProperty<Integer> resistanceAmplifier = new EnchantProperty<>(0, 1, 2);

    private final ArrowHitPlayerTemplate arrowHitPlayerTemplate;
    private final PlayerHitPlayerTemplate playerHitPlayerTemplate;

    @Inject
    public LastStand(PlayerHitPlayerTemplate playerHitPlayerTemplate, ArrowHitPlayerTemplate arrowHitPlayerTemplate) {
        this.playerHitPlayerTemplate = playerHitPlayerTemplate;
        this.arrowHitPlayerTemplate = arrowHitPlayerTemplate;
    }

    @Override
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        playerHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGER, PlayerInventory::getItemInMainHand);
        arrowHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGER, PlayerInventory::getItemInMainHand);
    }

    @Override
    public void execute(PostEventTemplateResult data, int level) {
        Player damaged = data.getDamager();

        if (damaged.getHealth() < 10)
            damaged.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80,
                    resistanceAmplifier.getValueAtLevel(level), true));
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
        return new LoreBuilder().declareVariable("I", "II", "III").write("Gain ").setColor(ChatColor.BLUE)
                .write("Resistance ").writeVariable(0, level).resetColor().write(" (4").next()
                .write("seconds) when reaching ").setColor(ChatColor.RED).write("3❤").build();
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