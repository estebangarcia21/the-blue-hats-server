package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.enchants.processedevents.PostEventTemplateResult;
import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
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

public class DiamondStomp implements DamageEnchant {
    private final EnchantProperty<Double> percentDamageIncrease = new EnchantProperty<>(0.7, 0.12, 0.25);

    private final DamageManager damageManager;
    private final PlayerHitPlayerTemplate playerHitPlayerTemplate;

    @Inject
    public DiamondStomp(DamageManager damageManager, PlayerHitPlayerTemplate playerHitPlayerTemplate) {
        this.damageManager = damageManager;
        this.playerHitPlayerTemplate = playerHitPlayerTemplate;
    }

    @Override
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        playerHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGER, PlayerInventory::getItemInMainHand,
                damageManager);
    }

    @Override
    public void execute(PostEventTemplateResult data, int level) {
        Player damaged = data.getDamagee();

        if (playerHasDiamondPiece(damaged)) {
            damageManager.addDamage(data.getEvent(), percentDamageIncrease.getValueAtLevel(level),
                    CalculationMode.ADDITIVE);
        }
    }

    private boolean playerHasDiamondPiece(Player player) {
        if (player.getInventory().getHelmet() != null) {
            if (player.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET) {
                return true;
            }
        }

        if (player.getInventory().getChestplate() != null) {
            if (player.getInventory().getHelmet().getType() == Material.DIAMOND_CHESTPLATE) {
                return true;
            }
        }

        if (player.getInventory().getLeggings() != null) {
            if (player.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS) {
                return true;
            }
        }

        if (player.getInventory().getBoots() != null) {
            if (player.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getName() {
        return "Diamond Stomp";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Diamondstomp";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("7%", "12%", "25%").write("Deal ").setColor(ChatColor.RED).write("+")
                .writeVariable(0, level).resetColor().write(" damage vs. players").next().write("wearing diamond armor")
                .build();
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
        return new Material[] { Material.GOLDEN_SWORD };
    }
}
