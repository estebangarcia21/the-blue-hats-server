package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.BowManager
import com.thebluehats.server.game.managers.enchants.*
import com.thebluehats.server.game.managers.enchants.Timer
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager
import com.thebluehats.server.game.utils.EnchantLoreParser
import com.thebluehats.server.game.utils.Var
import com.thebluehats.server.game.utils.add
import com.thebluehats.server.game.utils.varMatrix
import org.bukkit.Effect
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import java.util.*
import javax.inject.Inject

class Explosive @Inject constructor(
    private val regionManager: RegionManager,
    private val customEnchantUtils: CustomEnchantUtils,
    private val bowManager: BowManager,
    private val timer: Timer<UUID>
) : CustomEnchant, Listener {
    private val explosionRange = EnchantProperty(1.0, 2.5, 6.0)
    private val cooldownTime = EnchantProperty(5, 3, 5)
    private val explosionPitch = EnchantProperty(2f, 1f, 1.4f)
    private val explosionParticle = EnchantProperty(
        Effect.EXPLOSION_LARGE,
        Effect.EXPLOSION_HUGE, Effect.EXPLOSION_HUGE
    )

    override val name: String get() = "Explosive"
    override val enchantReferenceName: String get() = "explosive"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.BOW)

    @EventHandler
    fun onArrowLand(event: ProjectileHitEvent) {
        if (event.entity is Arrow) {
            val arrow = event.entity as Arrow

            if (event.entity.shooter is Player) {
                val bow = bowManager.getBowFromArrow(arrow)

                val data = customEnchantUtils.getItemEnchantData(this, bow)

                if (data.itemHasEnchant()) {
                    execute(data.enchantLevel, arrow.shooter as Player, arrow)
                }
            }
        }
    }

    fun execute(level: Int, shooter: Player, arrow: Arrow) {
        val range = explosionRange.getValueAtLevel(level)
        val playerUuid = shooter.uniqueId

        if (!timer.isRunning(playerUuid)) {
            arrow.getNearbyEntities(range, range, range).forEach { e ->
                if (e !is Player || regionManager.entityIsInSpawn(e)) return@forEach
                if (e !== shooter) return@forEach

                val force = e.location.toVector().subtract(arrow.location.toVector()).normalize().multiply(1.25)
                force.setY(.85f)

                e.velocity = force
            }

            arrow.world.playSound(
                arrow.location, Sound.EXPLODE, 0.75f,
                explosionPitch.getValueAtLevel(level)
            )
            arrow.world.playEffect(
                arrow.location, explosionParticle.getValueAtLevel(level),
                explosionParticle.getValueAtLevel(level).data, 100
            )
        }

        timer.start(playerUuid, (cooldownTime.getValueAtLevel(level) * 20).toLong(), false)
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Arrows go {0}! ({1}s cooldown)")

        val vars = varMatrix()

        vars add Var(0, "POP", "BANG", "BOOM")
        vars add Var(1, "5", "3", "5")

        enchantLoreParser.setVariables(vars)

        return enchantLoreParser.parseForLevel(level)
    }
}