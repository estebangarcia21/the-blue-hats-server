package com.thebluehats.server.game.enchants;

import java.util.ArrayList;
import java.util.UUID;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.Timer;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MegaLongBow implements CustomEnchant, Listener {
    private final EnchantProperty<Integer> amplifier = new EnchantProperty<>(1, 2, 3);

    private final CustomEnchantUtils customEnchantUtils;
    private final Timer<UUID> timer;

    @Inject
    public MegaLongBow(CustomEnchantUtils customEnchantUtils, Timer<UUID> timer) {
        this.customEnchantUtils = customEnchantUtils;
        this.timer = timer;
    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player && event.getProjectile() instanceof Arrow) {
            Player player = (Player) event.getEntity();
            Arrow arrow = (Arrow) event.getProjectile();

            ItemStack bow = player.getInventory().getItemInHand();

            if (customEnchantUtils.itemHasEnchant(this, bow)) {
               execute(player, arrow, customEnchantUtils.getEnchantLevel(this, bow));
            }
        }
    }

    public void execute(Player player, Arrow arrow, int level) {
        UUID playerUuid = player.getUniqueId();

        if (!timer.isRunning(playerUuid)) {
            arrow.setCritical(true);
            arrow.setVelocity(player.getLocation().getDirection().multiply(2.90));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, amplifier.getValueAtLevel(level)), true);
        }

        timer.start(playerUuid, 20, false);
    }

    @Override
    public String getName() {
        return "Mega Longbow";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Mlb";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "One shot per second, this bow is<br/>automatically fully drawn and<br/>grants <green>Jump Boost {0}</green> (2s)");

        enchantLoreParser.setSingleVariable("II", "III", "IV");

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
        return true;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.BOW };
    }
}