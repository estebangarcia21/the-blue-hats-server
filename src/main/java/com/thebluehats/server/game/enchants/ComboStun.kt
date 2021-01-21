package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.HitCounter
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import net.minecraft.server.v1_8_R3.IChatBaseComponent
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class ComboStun @Inject constructor(private val hitCounter: HitCounter, playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(arrayOf(playerDamageTrigger)) {
    private val duration = EnchantProperty(10, 16, 30)
    private val hitsNeeded = EnchantProperty(5, 4, 4)

    override val name: String get() = "Combo: Stun"
    override val enchantReferenceName: String get() = "Combostun"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val damager = data.damager
        val damagee = data.damagee
        val level = data.level
        hitCounter.addOne(damager)
        if (hitCounter.hasHits(damager, hitsNeeded.getValueAtLevel(level))) {
            val durationTime = duration.getValueAtLevel(level)

            damagee.addPotionEffect(PotionEffect(PotionEffectType.SLOW, durationTime, 8), true)
            damagee.addPotionEffect(PotionEffect(PotionEffectType.JUMP, durationTime, -8), true)
            damagee.world.playSound(damagee.location, Sound.ANVIL_LAND, 1f, 0.1f)

            sendPackets(damagee)
        }
    }

    private fun sendPackets(player: Player) {
        val chatTitle = IChatBaseComponent.ChatSerializer.a(
            "{\"text\": \"" + ChatColor.RED + "STUNNED!" + "\",color:" + ChatColor.GOLD.name.toLowerCase() + "}"
        )

        val title = PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle)
        val length = PacketPlayOutTitle(0, 60, 0)
        val chatSubTitle = IChatBaseComponent.ChatSerializer.a(
            "{\"text\": \"" + ChatColor.YELLOW
                    + "You cannot move!" + "\",color:" + ChatColor.GOLD.name.toLowerCase() + "}"
        )

        val subTitle = PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle)
        val subTitleLength = PacketPlayOutTitle(0, 60, 0)
        val playerConnection = (player as CraftPlayer).handle.playerConnection

        playerConnection.sendPacket(title)
        playerConnection.sendPacket(length)
        playerConnection.sendPacket(subTitle)
        playerConnection.sendPacket(subTitleLength)
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser =
            EnchantLoreParser("Every <yellow>{0}</yellow> strike on an enemy<br/>stuns them for {1} seconds")

        val vars = varMatrix()
        vars add Var(0, "fifth", "fourth", "fourth")
        vars add Var(1, "0.5", "0.8", "1.5")

        enchantLoreParser.setVariables(vars)

        return enchantLoreParser.parseForLevel(level)
    }
}