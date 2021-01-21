package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.enchants.*
import com.thebluehats.server.game.managers.enchants.Timer
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.util.Vector
import java.util.*

class DoubleJump @Inject constructor(
    private val customEnchantUtils: CustomEnchantUtils,
    private val timer: Timer<UUID>
) : CustomEnchant, GlobalTimerListener, Listener {
    private val cooldownTime = EnchantProperty(20, 10, 5)
    override fun onTick(player: Player) {
        val leggings = player.inventory.leggings
        if (player.gameMode != GameMode.SURVIVAL && player.gameMode != GameMode.ADVENTURE) {
            player.allowFlight = true
            return
        }
        player.allowFlight = customEnchantUtils.itemHasEnchant(this, leggings) && !timer.isRunning(player.uniqueId)
    }

    override fun getTickDelay(): Long {
        return 1L
    }

    @EventHandler
    fun onFlightAttempt(event: PlayerToggleFlightEvent) {
        val player = event.player
        if (event.player.gameMode == GameMode.SURVIVAL) event.isCancelled = true
        val leggings = player.inventory.leggings
        if (customEnchantUtils.itemHasEnchant(this, leggings)) {
            execute(player, customEnchantUtils.getEnchantLevel(this, leggings))
        }
    }

    fun execute(player: Player, level: Int) {
        val playerUuid = player.uniqueId
        val normalizedVelocity = player.eyeLocation.direction.normalize()
        if (!timer.isRunning(playerUuid)) {
            player.velocity = Vector(normalizedVelocity.x * 3, 1.5, normalizedVelocity.z * 3)
        }
        timer.start(playerUuid, (cooldownTime.getValueAtLevel(level) * 20).toLong(), false)
    }

    override fun getName(): String {
        return "Double-jump"
    }

    override fun getEnchantReferenceName(): String {
        return "Doublejump"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("You can double-jump. ({0}<br/>cooldown)")
        enchantLoreParser.setSingleVariable("20s", "10s", "5s")
        return enchantLoreParser.parseForLevel(level)
    }

    override fun isDisabledOnPassiveWorld(): Boolean {
        return false
    }

    override fun getEnchantGroup(): EnchantGroup {
        return EnchantGroup.B
    }

    override fun isRareEnchant(): Boolean {
        return true
    }

    override fun getEnchantItemTypes(): Array<Material> {
        return arrayOf(Material.LEATHER_LEGGINGS)
    }
}