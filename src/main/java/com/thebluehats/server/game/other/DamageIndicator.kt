package com.thebluehats.server.game.other

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerifier
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerifier
import net.minecraft.server.v1_8_R3.IChatBaseComponent
import net.minecraft.server.v1_8_R3.PacketPlayOutChat
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.text.DecimalFormat

class DamageIndicator @Inject constructor(
    private val damageManager: DamageManager,
    private val playerHitPlayerVerifier: PlayerHitPlayerVerifier,
    private val arrowHitPlayerVerifier: ArrowHitPlayerVerifier
) : Listener {
    @EventHandler
    fun onMeleeHit(event: EntityDamageByEntityEvent) {
        val isArrowHit = arrowHitPlayerVerifier.verify(event)
        if (playerHitPlayerVerifier.verify(event) || isArrowHit) {
            val damagee = event.entity as Player
            val damager = (if (isArrowHit) (event.damager as Arrow).shooter else event.damager) as Player
            displayIndicator(damager, damagee, damageManager.getFinalDamageFromEvent(event))
        }
    }

    private fun displayIndicator(damager: Player, damaged: Player, damageTaken: Double) {
        val packet = PacketPlayOutChat(
            IChatBaseComponent.ChatSerializer.a(
                "{\"text\":\"" +
                        buildIndicator(damaged, damageTaken) + "\"}"
            ), 2.toByte()
        )
        (damager as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

    private fun buildIndicator(damaged: Player, damageTaken: Double): String {
        val health = damaged.health.toInt() / 2
        val maxHealth = damaged.maxHealth.toInt() / 2
        val absorptionHearts = (damaged as CraftPlayer).handle.absorptionHearts.toInt() / 2
        val roundedDamageTaken = damageTaken.toInt()
        val indicatorString = StringBuilder()

        // TODO Get rank
        indicatorString.append(damaged.getName()).append(" ")
        for (i in 0 until Math.max(health - roundedDamageTaken, 0)) {
            indicatorString.append(ChatColor.DARK_RED.toString()).append("❤")
        }
        for (i in 0 until roundedDamageTaken - absorptionHearts) {
            indicatorString.append(ChatColor.RED.toString()).append("❤")
        }
        for (i in health until maxHealth) {
            indicatorString.append(ChatColor.BLACK.toString()).append("❤")
        }
        for (i in 0 until absorptionHearts) {
            indicatorString.append(ChatColor.YELLOW.toString()).append("❤")
        }
        indicatorString.append(ChatColor.RED.toString()).append(" ")
            .append(DecimalFormat("###0.000").format(damageTaken / 2)).append("HP")
        return indicatorString.toString()
    }
}