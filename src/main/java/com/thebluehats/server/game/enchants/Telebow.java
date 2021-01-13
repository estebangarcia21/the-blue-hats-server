package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.managers.enchants.*;
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.UUID;

public class Telebow implements CustomEnchant, Listener {
    private final EnchantProperty<Integer> cooldownTimes = new EnchantProperty<>(90, 45, 20);
    private final ArrayList<UUID> crouchingPlayers = new ArrayList<>();

    private final RegionManager regionManager;
    private final CustomEnchantUtils customEnchantUtils;
    private final Timer<UUID> timer;

    @Inject
    public Telebow(RegionManager regionManager, CustomEnchantUtils customEnchantUtils, Timer<UUID> timer) {
        this.regionManager = regionManager;
        this.customEnchantUtils = customEnchantUtils;
        this.timer = timer;
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player && event.getProjectile() instanceof Arrow) {
            Player player = (Player) event.getEntity();

            ItemStack bow = player.getInventory().getItemInHand();

            if (customEnchantUtils.itemHasEnchant(this, bow)) {
                if (player.isSneaking()) {
                    crouchingPlayers.add(player.getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow projectile = (Arrow) event.getEntity();

            if (projectile.getShooter() instanceof Player) {
                Player shooter = (Player) event.getEntity().getShooter();
                UUID shooterUuid = shooter.getUniqueId();

                ItemStack bow = shooter.getInventory().getItemInHand();

                if (timer.isRunning(shooter.getUniqueId())) {
                    sendCooldownMessage(shooter);

                    crouchingPlayers.remove(shooterUuid);

                    return;
                }

                CustomEnchantUtils.ItemEnchantData data = customEnchantUtils.getItemEnchantData(this, bow);

                if (data.itemHasEnchant()) {
                    execute(data.getEnchantLevel(), shooter, projectile);
                }
            }
        }
    }

    public void execute(int level, Player player, Arrow arrow) {
        UUID playerUuid = player.getUniqueId();

        if (!timer.isRunning(playerUuid) && !regionManager.entityIsInSpawn(arrow) && crouchingPlayers.contains(playerUuid)) {
            player.teleport(arrow);
            player.getWorld().playSound(arrow.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 2f);

            crouchingPlayers.remove(playerUuid);
        }

        timer.start(playerUuid, cooldownTimes.getValueAtLevel(level) * 20, true);
    }

    private void sendCooldownMessage(Player player) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\""
                + ChatColor.RED + "Telebow Cooldown: " + timer.getTime(player.getUniqueId()) / 20 + "(s)" + "\"}"),
                (byte) 2);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public String getName() {
        return "Telebow";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Telebow";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("Sneak to shoot a teleportation<br/>arrow ({0} cooldown, -3 per bow<br/>hit)");

        enchantLoreParser.setSingleVariable("90s", "45s", "20s");

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
