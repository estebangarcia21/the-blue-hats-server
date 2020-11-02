package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.combat.templates.EventTemplate;
import com.thebluehats.server.game.utils.LoreBuilder;
import com.thebluehats.server.game.managers.combat.DamageManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;

public class DiamondStomp extends CustomEnchant {
    private final EnchantProperty<Double> percentDamageIncrease = new EnchantProperty<>(0.7, 0.12, 0.25);

    private DamageManager manager;

    public DiamondStomp(DamageManager manager, EventTemplate... templates) {
        super(templates);

        this.manager = manager;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        runEventTemplates(this, event.getDamager(), event.getEntity(), PlayerInventory::getItemInMainHand,
                level -> executeEnchant((Player) event.getDamager(), level, event));
    }

    public void executeEnchant(Player player, int level, EntityDamageByEntityEvent event) {
        if (playerHasDiamondPiece(player)) {
            // manager.addDamage(event, percentDamageIncrease.getValueAtLevel(level),
            // CalculationMode.ADDITIVE);
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
            return player.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS;
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