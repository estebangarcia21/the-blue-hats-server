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
) : CustomEnchant, GlobalPlayerTimerListener, Listener {
    private val cooldownTime = EnchantProperty(20, 10, 5)

    override val name: String get() = "Double-jump"
    override val enchantReferenceName: String get() = "double-jump"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.LEATHER_LEGGINGS)

    override val tickDelay: Long get() = 1L

    override fun onTimeStep(player: Player) {
        val leggings = player.inventory.leggings
        if (player.gameMode != GameMode.SURVIVAL && player.gameMode != GameMode.ADVENTURE) {
            player.allowFlight = true
            return
        }

        player.allowFlight = customEnchantUtils.itemHasEnchant(this, leggings) && !timer.isRunning(player.uniqueId)
    }

    @EventHandler
    fun onFlightAttempt(event: PlayerToggleFlightEvent) {
        val player = event.player

        if (event.player.gameMode == GameMode.SURVIVAL) event.isCancelled = true

        val data = customEnchantUtils.getItemEnchantData(this, player.inventory.leggings)

        if (data.itemHasEnchant()) {
            execute(player, data.enchantLevel)
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

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("You can double-jump. ({0}<br/>cooldown)")

        enchantLoreParser.setSingleVariable("20s", "10s", "5s")

        return enchantLoreParser.parseForLevel(level)
    }
}