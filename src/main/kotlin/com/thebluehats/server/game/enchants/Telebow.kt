package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.enchants.*
import com.thebluehats.server.game.managers.enchants.Timer
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager
import com.thebluehats.server.game.utils.EnchantLoreParser
import net.minecraft.server.v1_8_R3.IChatBaseComponent
import net.minecraft.server.v1_8_R3.PacketPlayOutChat
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import java.util.*
import javax.inject.Inject

class Telebow @Inject constructor(
    private val regionManager: RegionManager,
    private val customEnchantUtils: CustomEnchantUtils,
    private val timer: Timer<UUID>
) : CustomEnchant, Listener {
    private val cooldownTimes = EnchantProperty(90, 45, 20)
    private val crouchingPlayers = ArrayList<UUID>()

    override val name: String get() = "Telebow"
    override val enchantReferenceName: String get() = "Telebow"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.A
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.BOW)

    @EventHandler
    fun onBowShoot(event: EntityShootBowEvent) {
        if (event.entity is Player && event.projectile is Arrow) {
            val player = event.entity as Player
            val bow = player.inventory.itemInHand
            if (customEnchantUtils.itemHasEnchant(this, bow)) {
                if (player.isSneaking) {
                    crouchingPlayers.add(player.uniqueId)
                }
            }
        }
    }

    @EventHandler
    fun onArrowHit(event: ProjectileHitEvent) {
        if (event.entity is Arrow) {
            val projectile = event.entity as Arrow
            if (projectile.shooter is Player) {
                val shooter = event.entity.shooter as Player
                val shooterUuid = shooter.uniqueId
                val bow = shooter.inventory.itemInHand
                if (timer.isRunning(shooter.uniqueId)) {
                    sendCooldownMessage(shooter)
                    crouchingPlayers.remove(shooterUuid)
                    return
                }
                val data = customEnchantUtils.getItemEnchantData(this, bow)
                if (data.itemHasEnchant()) {
                    execute(data.enchantLevel, shooter, projectile)
                }
            }
        }
    }

    fun execute(level: Int, player: Player, arrow: Arrow) {
        val playerUuid = player.uniqueId
        if (!timer.isRunning(playerUuid) && !regionManager.entityIsInSpawn(arrow) && crouchingPlayers.contains(
                playerUuid
            )
        ) {
            player.teleport(arrow)
            player.world.playSound(arrow.location, Sound.ENDERMAN_TELEPORT, 1f, 2f)
            crouchingPlayers.remove(playerUuid)
        }
        timer.start(playerUuid, (cooldownTimes.getValueAtLevel(level) * 20).toLong(), true)
    }

    private fun sendCooldownMessage(player: Player) {
        val packet = PacketPlayOutChat(
            IChatBaseComponent.ChatSerializer.a(
                "{\"text\":\""
                    + ChatColor.RED + "Telebow Cooldown: " + timer.getTime(player.uniqueId) / 20 + "(s)" + "\"}"
            ),
            2.toByte()
        )
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser =
            EnchantLoreParser("Sneak to shoot a teleportation<br/>arrow ({0} cooldown, -3 per bow<br/>hit)")
        enchantLoreParser.setSingleVariable("90s", "45s", "20s")
        return enchantLoreParser.parseForLevel(level)
    }
}
