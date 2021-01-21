package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.BowManager
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerifier
import com.thebluehats.server.game.managers.enchants.CustomEnchant
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*
import javax.inject.Inject

class LuckyShot @Inject constructor(
    private val customEnchantUtils: CustomEnchantUtils,
    private val bowManager: BowManager,
    private val arrowHitPlayerVerifier: ArrowHitPlayerVerifier
) : CustomEnchant, Listener {
    private val percentChance = EnchantProperty(0.02f, 0.05f, 0.1f)
    private val random = Random()
    @EventHandler
    fun onArrowHit(event: EntityDamageByEntityEvent) {
        if (arrowHitPlayerVerifier.verify(event)) {
            val arrow = event.damager as Arrow
            val player = arrow.shooter as Player
            val bow = bowManager.getBowFromArrow(arrow)
            if (customEnchantUtils.itemHasEnchant(this, bow)) {
                val chance = percentChance.getValueAtLevel(customEnchantUtils.getEnchantLevel(this, bow))
                val randomValue = random.nextFloat()
                if (randomValue <= chance) {
                    player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "LUCKY SHOT!" + ChatColor.LIGHT_PURPLE + " Quadruple damage!")
                }
            }
        }
    }

    override fun getName(): String {
        return "Lucky Shot"
    }

    override fun getEnchantReferenceName(): String {
        return "Luckyshot"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("<yellow>{0}</yellow> chance for a shot to deal<br/>quadruple damage")
        enchantLoreParser.setSingleVariable("2%", "5%", "10%")
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