package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.enchants.*
import com.thebluehats.server.game.managers.enchants.Timer
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class MegaLongBow @Inject constructor(
    private val customEnchantUtils: CustomEnchantUtils,
    private val timer: Timer<UUID>
) : CustomEnchant, Listener {
    private val amplifier = EnchantProperty(1, 2, 3)

    override val name: String get() = "Mega Longbow"
    override val enchantReferenceName: String get() = "mlb"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.BOW)

    @EventHandler
    fun onArrowShoot(event: EntityShootBowEvent) {
        if (event.entity is Player && event.projectile is Arrow) {
            val player = event.entity as Player
            val arrow = event.projectile as Arrow
            val bow = player.inventory.itemInHand

            val data = customEnchantUtils.getItemEnchantData(this, bow)

            if (data.itemHasEnchant()) execute(player, arrow, data.enchantLevel)
        }
    }

    fun execute(player: Player, arrow: Arrow, level: Int) {
        val playerUuid = player.uniqueId

        if (!timer.isRunning(playerUuid)) {
            arrow.isCritical = true
            arrow.velocity = player.location.direction.multiply(2.9)
            player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 40, amplifier.getValueAtLevel(level)), true)
        }

        timer.start(playerUuid, 20)
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "One shot per second, this bow is<br/>automatically fully drawn and<br/>grants <green>Jump Boost {0}</green> (2s)"
        )

        enchantLoreParser.setSingleVariable("II", "III", "IV")

        return enchantLoreParser.parseForLevel(level)
    }
}