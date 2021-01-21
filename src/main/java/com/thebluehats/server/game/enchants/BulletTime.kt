package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import com.thebluehats.server.game.utils.EntityValidator
import org.bukkit.Effect
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Arrow
import java.util.*

class BulletTime @Inject constructor(private val damageManager: DamageManager, arrowDamageTrigger: ArrowDamageTrigger) :
    DamageTriggeredEnchant(
        arrayOf<DamageEnchantTrigger>(arrowDamageTrigger), arrayOf<EntityValidator>(
            damageManager
        )
    ) {
    private val healingAmount = EnchantProperty(0, 2, 3)
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
            damagee.health = Math.min(
                damagee.health + healingAmount.getValueAtLevel(data.level),
                damagee.maxHealth
            )
        }
    }

    override fun getName(): String {
        return "Bullet Time"
    }

    override fun getEnchantReferenceName(): String {
        return "Bullettime"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Blocking destroys arrows that hit<br/>you")
        enchantLoreParser.addTextIf(level != 1, ". Destroying arrows this way heals <red>{0}❤</red>")
        enchantLoreParser.setSingleVariable("", "1❤", "1.5❤")
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
        return arrayOf(Material.GOLD_SWORD)
    }

    override fun getEnchantHolder(): EnchantHolder {
        return EnchantHolder.DAMAGEE
    }
}