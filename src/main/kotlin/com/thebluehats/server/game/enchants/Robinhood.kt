package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.BowManager
import com.thebluehats.server.game.managers.combat.CalculationMode
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerifier
import com.thebluehats.server.game.managers.enchants.CustomEnchant
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import javax.inject.Inject

class Robinhood @Inject constructor(
    private val plugin: JavaPlugin,
    private val customEnchantUtils: CustomEnchantUtils,
    private val damageManager: DamageManager,
    private val bowManager: BowManager,
    private val arrowHitPlayerVerifier: ArrowHitPlayerVerifier
) : CustomEnchant, Listener {
    private val damageReduction = EnchantProperty(.4f, .5f, .6f)
    private val arrowTasks = HashMap<Arrow, Int>()
    private val ROBINHOOD_RANGE = 8.0

    override val name: String get() = "Robinhood"
    override val enchantReferenceName: String get() = "robinhood"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.BOW)

    @EventHandler
    fun onShoot(event: EntityShootBowEvent) {
        if (event.entity is Player) {
            val arrow = event.projectile as Arrow
            val bow = bowManager.getBowFromArrow(arrow)
            if (customEnchantUtils.itemHasEnchant(this, bow)) {
                execute(customEnchantUtils.getEnchantLevel(this, bow), arrow, arrow.shooter as Player, event.force)
            }
        }
    }

    @EventHandler
    fun onHit(event: EntityDamageByEntityEvent) {
        if (arrowHitPlayerVerifier.verify(event)) {
            var removal: Arrow? = null
            for ((key) in arrowTasks) {
                if (key === event.damager) {
                    val arrow = event.damager as Arrow
                    if (arrow.shooter is Player) {
                        val player = arrow.shooter as Player
                        val bow = player.inventory.itemInHand
                        if (customEnchantUtils.itemHasEnchant(this, bow)) {
                            damageManager.addDamage(
                                event,
                                damageReduction.getValueAtLevel(customEnchantUtils.getEnchantLevel(this, bow))
                                    .toDouble(),
                                CalculationMode.ADDITIVE
                            )
                        }
                    }
                    if (!arrow.isValid) {
                        Bukkit.getServer().scheduler.cancelTask(arrowTasks[arrow]!!)
                        removal = arrow
                    }
                }
            }
            if (removal != null) arrowTasks.remove(removal)
        }
    }

    @EventHandler
    fun onArrowLand(event: ProjectileHitEvent) {
        if (event.entity is Arrow) {
            var removal: Arrow? = null
            for ((key) in arrowTasks) {
                if (key === event.entity) {
                    val arrow = event.entity as Arrow
                    if (!arrow.isValid) {
                        Bukkit.getServer().scheduler.cancelTask(arrowTasks[arrow]!!)
                        removal = arrow
                    }
                }
            }
            if (removal != null) arrowTasks.remove(removal)
        }
    }

    fun execute(level: Int, arrow: Arrow, player: Player, force: Float) {
        if (level == 1 && force < 1) return
        arrowTasks[arrow] = Bukkit.getServer().scheduler.scheduleSyncRepeatingTask(plugin, {
            var closestEntities = player.getNearbyEntities(ROBINHOOD_RANGE, ROBINHOOD_RANGE, ROBINHOOD_RANGE)
            val closestPlayers = ArrayList<Player>()
            for (entity in closestEntities) {
                if (entity is Player) {
                    closestPlayers.add(entity)
                }
            }
            if (closestPlayers.isEmpty()) closestEntities =
                arrow.getNearbyEntities(ROBINHOOD_RANGE, ROBINHOOD_RANGE, ROBINHOOD_RANGE)
            var closestPlayer: Player? = null
            for (entity in closestEntities) {
                if (entity is Player) {
                    if (entity !== player) {
                        if (closestPlayer == null) {
                            closestPlayer = entity
                            continue
                        }
                        if (player.location.toVector()
                                .distance(entity.getLocation().toVector()) < player.location.toVector()
                                .distance(closestPlayer.location.toVector())
                        ) {
                            closestPlayer = entity
                        }
                    }
                }
            }
            if (closestPlayer == null) return@scheduleSyncRepeatingTask
            val arrowVector = arrow.location.toVector()
            val closestPlayerVector = closestPlayer.location.toVector()
            closestPlayerVector.y = closestPlayerVector.y + 2
            val direction = closestPlayerVector.subtract(arrowVector).normalize()
            arrow.velocity = direction
        }, 0L, 3L)
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("")
        enchantLoreParser.addTextIf(level == 1, "Your charged shots are homing but<br/>deal <red>{0}</red> damage")
        enchantLoreParser.addTextIf(level != 1, "All your shots are homing but<br/>deal <red>{0}</red> damage")
        enchantLoreParser.setSingleVariable("40%", "50%", "60%")
        return enchantLoreParser.parseForLevel(level)
    }
}