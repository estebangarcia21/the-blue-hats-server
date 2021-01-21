package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.enchants.*
import com.thebluehats.server.game.managers.enchants.Timer
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

class BooBoo @Inject constructor(private val customEnchantUtils: CustomEnchantUtils, private val timer: Timer<UUID>) :
    CustomEnchant, GlobalTimerListener {
    private val secondsNeeded = EnchantProperty(5, 4, 3)
    override fun onTick(player: Player) {
        val leggings = player.inventory.leggings
        val data = customEnchantUtils.getItemEnchantData(this, leggings)
        if (data.itemHasEnchant()) {
            val ticks = (secondsNeeded.getValueAtLevel(data.enchantLevel) * 20).toLong()
            timer.start(player.uniqueId, ticks, false) { execute(player) }
        }
    }

    override fun getTickDelay(): Long {
        return 1L
    }

    fun execute(player: Player) {
        if (!customEnchantUtils.itemHasEnchant(this, player.inventory.leggings)) return
        player.health = Math.min(player.health + 2, player.maxHealth)
    }

    override fun getName(): String {
        return "Boo-boo"
    }

    override fun getEnchantReferenceName(): String {
        return "Booboo"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Passively regain <red>1❤</red> every {0}<br/>seconds")
        enchantLoreParser.setSingleVariable("5", "4", "3")
        return enchantLoreParser.parseForLevel(level)
    }

    override fun isDisabledOnPassiveWorld(): Boolean {
        return false
    }

    override fun getEnchantGroup(): EnchantGroup {
        return EnchantGroup.B
    }

    override fun isRareEnchant(): Boolean {
        return false
    }

    override fun getEnchantItemTypes(): Array<Material> {
        return arrayOf(Material.LEATHER_LEGGINGS)
    }
}