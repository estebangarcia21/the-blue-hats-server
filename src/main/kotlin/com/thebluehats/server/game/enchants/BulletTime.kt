package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Effect
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Arrow
import java.util.*

class BulletTime @Inject constructor(
    private val damageManager: DamageManager, arrowDamageTrigger:
    ArrowDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf(arrowDamageTrigger), arrayOf(damageManager)
) {
    private val healingAmount = EnchantProperty(0, 2, 3)

    override val name: String get() = "Bullet Time"
    override val enchantReferenceName: String get() = "bullet-time"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGEE

    override fun execute(data: DamageEventEnchantData) {
        val event = data.event
        val damagee = data.damagee
        val arrow = event.damager as Arrow

        if (damagee.isBlocking) {
            damageManager.setEventAsCanceled(event)

            arrow.knockbackStrength = 0
            arrow.setBounce(true)

            damagee.world.playSound(damagee.location, Sound.FIZZ, 1f, 1.5f)

            arrow.world.playEffect(arrow.location, Effect.EXPLOSION, 0, 30)
            arrow.remove()

            damagee.health =
                (damagee.health + healingAmount.getValueAtLevel(data.level)).coerceAtMost(damagee.maxHealth)
        }
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Blocking destroys arrows that hit<br/>you")

        enchantLoreParser.addTextIf(level != 1, ". Destroying arrows this way heals <red>{0}❤</red>")
        enchantLoreParser.setSingleVariable("", "1❤", "1.5❤")

        return enchantLoreParser.parseForLevel(level)
    }
}