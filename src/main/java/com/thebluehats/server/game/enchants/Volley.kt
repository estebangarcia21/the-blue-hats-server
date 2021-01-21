package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.BowManager
import com.thebluehats.server.game.managers.enchants.CustomEnchant
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import javax.inject.Inject

class Volley @Inject constructor(
    private val plugin: JavaPlugin,
    private val regionManager: RegionManager,
    private val bowManager: BowManager,
    private val customEnchantUtils: CustomEnchantUtils
) : CustomEnchant, Listener {
    private val arrows = EnchantProperty(2, 3, 4)
    private val volleyTasks = HashMap<Arrow, Int>()
    private val arrowCount = HashMap<Arrow, Int>()

    @EventHandler
    fun onBowShoot(event: EntityShootBowEvent) {
        if (event.projectile is Arrow) {
            val eventArrow = event.projectile as Arrow
            for (arrow in volleyTasks.keys) {
                if (eventArrow.shooter == arrow.shooter) {
                    return
                }
            }
            if (eventArrow.shooter is Player) {
                val player = eventArrow.shooter as Player
                val bow = bowManager.getBowFromArrow(eventArrow)
                val data = customEnchantUtils.getItemEnchantData(this, bow)
                if (data.itemHasEnchant()) {
                    execute(data.enchantLevel, player, eventArrow, event.force)
                }
            }
        }
    }

    fun execute(level: Int, player: Player, arrow: Arrow, force: Float) {
        val item = player.inventory.itemInHand
        val originalVelocity = arrow.velocity
        Bukkit.getServer().scheduler.scheduleSyncDelayedTask(plugin, {
            volleyTasks[arrow] = Bukkit.getServer().scheduler.scheduleSyncRepeatingTask(plugin, {
                if (!regionManager.entityIsInSpawn(player)) {
                    player.world.playSound(player.location, Sound.SHOOT_ARROW, 1f, 1f)
                    val volleyArrow = player.launchProjectile(Arrow::class.java)
                    volleyArrow.velocity = player.eyeLocation.direction.normalize().multiply(originalVelocity.length())
                    val event = EntityShootBowEvent(player, item, volleyArrow, force)
                    plugin.server.pluginManager.callEvent(event)
                    bowManager.registerArrow(volleyArrow, player)
                    arrowCount[arrow] = arrowCount.getOrDefault(arrow, 1) + 1
                    if (arrowCount[arrow]!! > arrows.getValueAtLevel(level)) {
                        Bukkit.getServer().scheduler.cancelTask(volleyTasks[arrow]!!)
                        volleyTasks.remove(arrow)
                        arrowCount.remove(arrow)
                    }
                }
            }, 0L, 1)
        }, 2L)
    }

    override fun getName(): String {
        return "Volley"
    }

    override fun getEnchantReferenceName(): String {
        return "Volley"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Shoot <white>{0}</white> arrows at once")

        enchantLoreParser.setSingleVariable("3", "4", "5")

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