package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.BowManager
import com.thebluehats.server.game.managers.enchants.*
import com.thebluehats.server.game.managers.enchants.Timer
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager
import com.thebluehats.server.game.utils.EnchantLoreParser
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

    @EventHandler
    fun onArrowLand(event: ProjectileHitEvent) {
        if (event.entity is Arrow) {
            val arrow = event.entity as Arrow
            if (event.entity.shooter is Player) {
                val bow = bowManager.getBowFromArrow(arrow)
                if (customEnchantUtils.itemHasEnchant(this, bow)) {
                    execute(customEnchantUtils.getEnchantLevel(this, bow), arrow.shooter as Player, arrow)
                }
            }
        }
    }

    fun execute(level: Int, shooter: Player, arrow: Arrow) {
        val playerUuid = shooter.uniqueId
        if (!timer.isRunning(playerUuid)) {
            for (entity in arrow.getNearbyEntities(
                explosionRange.getValueAtLevel(level),
                explosionRange.getValueAtLevel(level), explosionRange.getValueAtLevel(level)
            )) {
                if (entity is Player) {
                    val player = entity
                    if (regionManager.entityIsInSpawn(player)) continue
                    if (player !== shooter) {
                        val force = player.location.toVector().subtract(arrow.location.toVector())
                            .normalize().multiply(1.25)
                        force.setY(.85f)
                        player.velocity = force
                    }
                }
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
        timer.start(playerUuid, (cooldownTime.getValueAtLevel(level) * 20).toLong(), true)
    }

    override fun getName(): String {
        return "Explosive"
    }

    override fun getEnchantReferenceName(): String {
        return "Explosive"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Arrows go {0}! ({1}s cooldown)")
        val variables: Array<Array<String>> = arrayOfNulls(2)
        variables[0] = arrayOf("POP", "BANG", "BOOM")
        variables[1] = arrayOf("5", "3", "5")
        enchantLoreParser.setVariables(variables)
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
        return arrayOf(Material.BOW)
    }
}