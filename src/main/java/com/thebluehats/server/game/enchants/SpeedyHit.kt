package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.Timer
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import com.thebluehats.server.game.utils.Var
import com.thebluehats.server.game.utils.add
import com.thebluehats.server.game.utils.varMatrix
import org.bukkit.Material
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class SpeedyHit @Inject constructor(private val timer: Timer<UUID>, playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(arrayOf<DamageEnchantTrigger>(playerDamageTrigger)) {
    private val speedDuration = EnchantProperty(5, 7, 9)
    private val cooldownTime = EnchantProperty(3, 2, 1)

    override val name: String get() = "Speedy Hit"
    override val enchantReferenceName: String get() = "Speedyhit"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.A
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val damager = data.damager
        val playerUuid = damager.uniqueId
        val level = data.level
        if (!timer.isRunning(playerUuid)) {
            damager.addPotionEffect(
                PotionEffect(PotionEffectType.SPEED, speedDuration.getValueAtLevel(level) * 20, 0, true)
            )
        }
        timer.start(playerUuid, (cooldownTime.getValueAtLevel(level) * 20).toLong(), false)
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Gain Speed I for <yellow>{0}s</yellow> on hit({1}s<br/>cooldown)"
        )
        val variables = varMatrix()
        variables add Var( 0,"5", "7", "9")
        variables add Var( 1,"3", "2", "1")
        enchantLoreParser.setVariables(variables)
        return enchantLoreParser.parseForLevel(level)
    }
}