package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.enchants.processedevents.PostEventTemplateResult;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.TargetPlayer;
import com.thebluehats.server.game.managers.enchants.DamageEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.LoreBuilder;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;

public class Healer implements DamageEnchant {
    private final EnchantProperty<Integer> healAmount = new EnchantProperty<>(2, 4, 6);

    private final PlayerHitPlayerTemplate playerHitPlayerTemplate;

    @Inject
    public Healer(PlayerHitPlayerTemplate playerHitPlayerTemplate) {
        this.playerHitPlayerTemplate = playerHitPlayerTemplate;
    }

    @Override
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        playerHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGER, PlayerInventory::getItemInMainHand);
    }

    @Override
    public void execute(PostEventTemplateResult data, int level) {
        Player damager = data.getDamager();
        Player damaged = data.getDamagee();

        damager.setHealth(Math.min(damager.getHealth() + healAmount.getValueAtLevel(level),
                damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
        damaged.setHealth(Math.min(damaged.getHealth() + healAmount.getValueAtLevel(level),
                damaged.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
    }

    @Override
    public String getName() {
        return "Healer";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Healer";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("1❤", "2❤", "3❤").write("Hitting a player ").setColor(ChatColor.GREEN)
                .write("heals").resetColor().write(" both you and them for ").setColor(ChatColor.RED)
                .writeVariable(0, level).build();
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
        return true;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.GOLDEN_SWORD };
    }
}