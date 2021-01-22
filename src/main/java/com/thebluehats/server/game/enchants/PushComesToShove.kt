package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.HitCounter
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import com.thebluehats.server.game.utils.Var
import com.thebluehats.server.game.utils.add
import com.thebluehats.server.game.utils.varMatrix
import org.bukkit.Material
import org.bukkit.entity.Arrow
import java.util.*
import javax.inject.Inject

class PushComesToShove @Inject constructor(private val hitCounter: HitCounter, arrowDamageTrigger: ArrowDamageTrigger) :
    DamageTriggeredEnchant(arrayOf(arrowDamageTrigger)) {
    private val pctsForce = EnchantProperty(12f, 25f, 35f)
    private val damageAmount = EnchantProperty(0f, 1f, 2f)

    override val name: String get() = "Push Comes to Shove"
    override val enchantReferenceName: String get() = "Pcts"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.BOW)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val damagee = data.damagee
        val level = data.level
        val arrow = data.event.damager as Arrow
        hitCounter.addOne(damagee)
        if (hitCounter.hasHits(damagee, 3)) {
            val velocity = arrow.velocity.normalize().multiply(pctsForce.getValueAtLevel(level) / 2.35)
            velocity.setY(0)
            damagee.velocity = velocity
            damagee.damage(damageAmount.getValueAtLevel(level).toDouble())
        }
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Every 3rd shot on a player<br/><aqua>{0}</aqua>")
        enchantLoreParser.addTextIf(level != 1, " and deals <red>{1}</red><br/>extra damage")

        val variables = varMatrix()

        variables add Var(0,"Punch", "Punch V", "Punch VII")
        variables add Var(1,"", "+0.5❤", "+1❤")

        enchantLoreParser.setVariables(variables)

        return enchantLoreParser.parseForLevel(level)
    }
}