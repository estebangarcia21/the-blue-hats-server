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
    DamageTriggeredEnchant(arrayOf<DamageEnchantTrigger>(playerDamageTrigger)) {
    private val duration = EnchantProperty(10, 16, 30)
    private val hitsNeeded = EnchantProperty(5, 4, 4)
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

    override fun getName(): String {
        return "Combo: Stun"
    }

    override fun getEnchantReferenceName(): String {
        return "Combostun"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser =
            EnchantLoreParser("Every <yellow>{0}</yellow> strike on an enemy<br/>stuns them for {1} seconds")
        val variables: Array<Array<String>> = arrayOfNulls(2)
        variables[0] = arrayOf("fifth", "fourth", "fourth")
        variables[1] = arrayOf("0.5", "0.8", "1.5")
        enchantLoreParser.setVariables(variables)
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
        return arrayOf(Material.GOLD_SWORD)
    }

    override fun getEnchantHolder(): EnchantHolder {
        return EnchantHolder.DAMAGER
    }
}