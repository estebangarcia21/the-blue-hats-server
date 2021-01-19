package com.thebluehats.server.game.enchants;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.*;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

public class DoubleJump implements CustomEnchant, GlobalTimerListener, Listener {
    private final EnchantProperty<Integer> cooldownTime = new EnchantProperty<>(20, 10, 5);

    private final CustomEnchantUtils customEnchantUtils;
    private final Timer<UUID> timer;

    @Inject
    public DoubleJump(CustomEnchantUtils customEnchantUtils, Timer<UUID> timer) {
        this.customEnchantUtils = customEnchantUtils;
        this.timer = timer;
    }

    @Override
    public void onTick(Player player) {
        ItemStack leggings = player.getInventory().getLeggings();

        if (player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE) {
            player.setAllowFlight(true);

            return;
        }

        player.setAllowFlight(
                customEnchantUtils.itemHasEnchant(this, leggings) && !timer.isRunning(player.getUniqueId()));
    }

    @Override
    public long getTickDelay() {
        return 1L;
    }

    @EventHandler
    public void onFlightAttempt(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        if (event.getPlayer().getGameMode() == GameMode.SURVIVAL)
            event.setCancelled(true);

        ItemStack leggings = player.getInventory().getLeggings();

        if (customEnchantUtils.itemHasEnchant(this, leggings)) {
            execute(player, customEnchantUtils.getEnchantLevel(this, leggings));
        }
    }

    public void execute(Player player, int level) {
        UUID playerUuid = player.getUniqueId();

        Vector normalizedVelocity = player.getEyeLocation().getDirection().normalize();

        if (!timer.isRunning(playerUuid)) {
            player.setVelocity(new Vector(normalizedVelocity.getX() * 3, 1.5, normalizedVelocity.getZ() * 3));
        }

        timer.start(playerUuid, cooldownTime.getValueAtLevel(level) * 20, false);
    }

    @Override
    public String getName() {
        return "Double-jump";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Doublejump";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("You can double-jump. ({0}<br/>cooldown)");

        enchantLoreParser.setSingleVariable("20s", "10s", "5s");

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
        return new Material[] { Material.LEATHER_LEGGINGS };
    }
}
