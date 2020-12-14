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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Peroxide implements DamageEnchant {
    private final EnchantProperty<Integer> regenDuration = new EnchantProperty<>(5, 8, 8);
    private final EnchantProperty<Integer> regenAmplifier = new EnchantProperty<>(0, 0, 1);

    private final ArrowHitPlayerTemplate arrowHitPlayerTemplate;
    private final PlayerHitPlayerTemplate playerHitPlayerTemplate;

    @Inject
    public Peroxide(PlayerHitPlayerTemplate playerHitPlayerTemplate, ArrowHitPlayerTemplate arrowHitPlayerTemplate) {
        this.playerHitPlayerTemplate = playerHitPlayerTemplate;
        this.arrowHitPlayerTemplate = arrowHitPlayerTemplate;
    }

    @Override
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        playerHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGEE, PlayerInventory::getLeggings);
        arrowHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGEE, PlayerInventory::getLeggings);
    }

    @Override
    public void execute(PostEventTemplateResult data, int level) {
        data.getDamagee().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                regenDuration.getValueAtLevel(level) * 20, regenAmplifier.getValueAtLevel(level), true));
    }

    @Override
    public String getName() {
        return "Peroxide";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Peroxide";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("Regen I", "Regen I", "Regen II").declareVariable("5", "8", "8")
                .write("Gain ").writeVariable(ChatColor.RED, 0, level).write(" (").writeVariable(1, level).write("s)")
                .write(" when hit").build();
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