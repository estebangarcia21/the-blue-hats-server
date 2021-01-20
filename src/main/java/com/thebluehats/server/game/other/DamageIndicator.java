package com.thebluehats.server.game.other;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerifier;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerifier;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.text.DecimalFormat;

public class DamageIndicator implements Listener {
    private final PlayerHitPlayerVerifier playerHitPlayerVerifier;
    private final ArrowHitPlayerVerifier arrowHitPlayerVerifier;
    private final DamageManager damageManager;

    @Inject
    public DamageIndicator(DamageManager damageManager, PlayerHitPlayerVerifier playerHitPlayerVerifier,
            ArrowHitPlayerVerifier arrowHitPlayerVerifier) {
        this.damageManager = damageManager;
        this.playerHitPlayerVerifier = playerHitPlayerVerifier;
        this.arrowHitPlayerVerifier = arrowHitPlayerVerifier;
    }

    @EventHandler
    public void onMeleeHit(EntityDamageByEntityEvent event) {
        boolean isArrowHit = arrowHitPlayerVerifier.verify(event);

        if (playerHitPlayerVerifier.verify(event) || isArrowHit) {
            Player damagee = (Player) event.getEntity();
            Player damager = (Player) (isArrowHit ? ((Arrow) event.getDamager()).getShooter() : event.getDamager());

            displayIndicator(damager, damagee, damageManager.getFinalDamageFromEvent(event));
        }
    }

    private void displayIndicator(Player damager, Player damaged, double damageTaken) {
        PacketPlayOutChat packet = new PacketPlayOutChat(
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + buildIndicator(damaged, damageTaken) + "\"}"),
                (byte) 2);

        ((CraftPlayer) damager).getHandle().playerConnection.sendPacket(packet);
    }

    private String buildIndicator(Player damaged, double damageTaken) {
        int health = (int) damaged.getHealth() / 2;
        int maxHealth = (int) damaged.getMaxHealth() / 2;
        int absorptionHearts = (int) ((CraftPlayer) damaged).getHandle().getAbsorptionHearts() / 2;

        int roundedDamageTaken = (int) damageTaken;

        StringBuilder indicatorString = new StringBuilder();

        // TODO Get rank
        indicatorString.append(damaged.getName()).append(" ");

        for (int i = 0; i < Math.max(health - roundedDamageTaken, 0); i++) {
            indicatorString.append(ChatColor.DARK_RED.toString()).append("❤");
        }

        for (int i = 0; i < roundedDamageTaken - absorptionHearts; i++) {
            indicatorString.append(ChatColor.RED.toString()).append("❤");
        }

        for (int i = health; i < maxHealth; i++) {
            indicatorString.append(ChatColor.BLACK.toString()).append("❤");
        }

        for (int i = 0; i < absorptionHearts; i++) {
            indicatorString.append(ChatColor.YELLOW.toString()).append("❤");
        }

        indicatorString.append(ChatColor.RED.toString()).append(" ")
                .append(new DecimalFormat("###0.000").format(damageTaken)).append("HP");

        return indicatorString.toString();
    }
}
