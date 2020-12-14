package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.enchants.processedevents.ProcessedEntityDamageByEntityEvent;
import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.grindingsystem.GrindingSystem;
import com.thebluehats.server.game.utils.LoreBuilder;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;

public class Billionaire extends CustomEnchant {
    private final EnchantProperty<Double> damageIncrease = new EnchantProperty<>(1.33D, 1.67D, 2D);
    private final EnchantProperty<Integer> goldNeeded = new EnchantProperty<>(100, 200, 350);

    private final DamageManager damageManager;
    private final GrindingSystem grindingSystem;
    private final PlayerHitPlayerTemplate playerHitPlayerTemplate;

    @Inject
    public Billionaire(GrindingSystem grindingSystem, DamageManager damageManager,
            PlayerHitPlayerTemplate playerHitPlayerTemplate) {
        this.grindingSystem = grindingSystem;
        this.damageManager = damageManager;
        this.playerHitPlayerTemplate = playerHitPlayerTemplate;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        playerHitPlayerTemplate.run(this, event, PlayerInventory::getItemInMainHand, (e, level) -> run(e, level),
                damageManager);
    }

    public void run(ProcessedEntityDamageByEntityEvent event, int level) {
        Player damagwr = event.getDamager();
        
        if (grindingSystem.getPlayerGold() >=
        goldNeeded.getValueAtLevel(level)) {
        GrindingSystem.setPlayerGold(player,
        GrindingSystem.getPlayerGold(player) - goldNeeded.getValueAtLevel(level));

        DamageManager.addDamage((EntityDamageByEntityEvent) args.getEvent(),
        damageIncrease.getValueAtLevel(level), CalculationMode.MULTIPLICATIVE);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,
        0.73f);
    }

    @Override
    public String getName() {
        return "Billionaire";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Billionaire";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("1.33", "1.67", "2").declareVariable("100g", "200g", "350g")
                .setColor(ChatColor.GRAY).write("Hits with this sword deals ").setColor(ChatColor.RED)
                .writeVariable(0, level).write("x").next().setColor(ChatColor.RED).write("damage ")
                .setColor(ChatColor.GRAY).write("but cost ").setColor(ChatColor.GOLD).writeVariable(1, level).build();
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
        return new Material[] { Material.GOLDEN_SWORD };
    }
}