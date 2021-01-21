package com.thebluehats.server.game.managers.combat

import com.google.inject.Inject
import com.thebluehats.server.core.modules.annotations.MirrorReference
import com.thebluehats.server.game.enchants.Mirror
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager
import com.thebluehats.server.game.utils.DataInitializer
import com.thebluehats.server.game.utils.EntityValidator
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.potion.PotionEffectType
import java.util.*

class DamageManager @Inject constructor(
    @param:MirrorReference private val mirror: Mirror, private val combatManager: CombatManager,
    private val customEnchantUtils: CustomEnchantUtils, private val regionManager: RegionManager
) : EntityValidator, DataInitializer {
    private val eventData = HashMap<UUID, EventData>()
    private val canceledEvents = ArrayList<UUID>()
    private val removeCriticalDamage = ArrayList<UUID>()
    private val mirrorReflectionValues = EnchantProperty(0f, .25f, .5f)

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onHit(event: EntityDamageByEntityEvent) {
        val damagerUuid = event.damager.uniqueId

        if (canceledEvents.contains(damagerUuid)) {
            event.isCancelled = true
        } else {
            event.damage = getDamageFromEvent(event)
        }

        eventData.remove(damagerUuid)
        canceledEvents.remove(damagerUuid)
    }

    override fun validate(vararg entities: Array<Entity>): Boolean {
        for (entity in entities) {
            if (uuidIsInCanceledEvent(entity.uniqueId)) {
                return false
            }
        }
        return true
    }

    @EventHandler
    override fun initializeDataOnPlayerJoin(event: PlayerJoinEvent) {
        eventData[event.player.uniqueId] = EventData()
    }

    /*
     * ----------- Utility Methods ----------
     */
    fun getDamageFromEvent(event: EntityDamageByEntityEvent): Double {
        return calculateDamage(event.damage, event)
    }

    fun getFinalDamageFromEvent(event: EntityDamageByEntityEvent): Double {
        return calculateDamage(event.finalDamage, event)
    }

    /*
     * ---------- Manipulation methods --------------
     */
    fun addDamage(event: EntityDamageByEntityEvent, value: Double, mode: CalculationMode?) {
        val eventValues = eventData.computeIfAbsent(event.damager.uniqueId) { k: UUID? -> EventData() }
        when (mode) {
            CalculationMode.ADDITIVE -> eventValues.additiveDamage = eventValues.additiveDamage + value
            CalculationMode.MULTIPLICATIVE -> eventValues.multiplicativeDamage =
                eventValues.multiplicativeDamage + value
        }
    }

    fun reduceDamageByPercentage(event: EntityDamageByEntityEvent, value: Double) {
        val data = eventData.computeIfAbsent(event.damager.uniqueId) { k: UUID? -> EventData() }
        if (data.reductionAmount == 1.0) {
            data.reductionAmount = data.reductionAmount - (1 - value)
            return
        }
        data.reductionAmount = 1 - data.reductionAmount * value
    }

    fun doTrueDamage(target: Player, damage: Double) {
        if (!customEnchantUtils.itemHasEnchant(mirror, target.inventory.leggings)) {
            target.health = Math.max(0.0, target.health - damage)
            target.damage(0.0)
        }
    }

    fun doTrueDamage(target: Player, damage: Double, reflectTo: Player) {
        val data = customEnchantUtils.getItemEnchantData(mirror, target.inventory.leggings)
        combatManager.combatTag(target)
        if (data.itemHasEnchant()) {
            val level = data.enchantLevel
            if (level > 1) {
                val finalDamage = damage * mirrorReflectionValues.getValueAtLevel(level)
                combatManager.combatTag(reflectTo)
                reflectTo.damage(0.0)
                safeSetPlayerHealth(reflectTo, Math.max(0.0, reflectTo.health - finalDamage))
            }
        } else {
            target.damage(0.0)
            safeSetPlayerHealth(target, Math.max(0.0, target.health - damage))
        }
    }

    fun removeExtraCriticalDamage(event: EntityDamageByEntityEvent) {
        removeCriticalDamage.add(event.damager.uniqueId)
    }

    fun setEventAsCanceled(event: EntityDamageByEntityEvent) {
        canceledEvents.add(event.damager.uniqueId)
    }

    fun eventIsNotCancelled(event: EntityDamageByEntityEvent): Boolean {
        return !canceledEvents.contains(event.damager.uniqueId)
    }

    fun uuidIsInCanceledEvent(uuid: UUID): Boolean {
        return canceledEvents.contains(uuid)
    }

    fun isCriticalHit(player: Player): Boolean {
        return (player.fallDistance > 0 && !(player as Entity).isOnGround
                && player.location.block.type != Material.LADDER && player.location.block.type != Material.VINE && player.location.block.type != Material.WATER && player.location.block.type != Material.LAVA && player.vehicle == null && !player.hasPotionEffect(
            PotionEffectType.BLINDNESS
        ))
    }

    private fun calculateDamage(initialDamage: Double, event: EntityDamageByEntityEvent): Double {
        val data = eventData.computeIfAbsent(event.damager.uniqueId) { k: UUID? -> EventData() }
        var damage = (initialDamage * data.additiveDamage * data.multiplicativeDamage
                * data.reductionAmount)
        if (removeCriticalDamage.contains(event.damager.uniqueId)) {
            damage *= 2.0 / 3.0
        }
        if (event.entity is Player) {
            val damagee = event.entity as Player
            val leggings = damagee.inventory.leggings
            if (leggings != null) {
                val pantsLore = leggings.itemMeta.lore
                if (pantsLore != null) {
                    for (line in pantsLore) {
                        if (line.contains("As strong as iron")) {
                            damage *= 0.88
                        }
                    }
                }
            }
        }
        damage -= data.heartReductionAmount.toDouble()
        if (damage <= 0) damage = 1.0
        return damage
    }

    private fun safeSetPlayerHealth(player: Player, health: Double) {
        if (!regionManager.entityIsInSpawn(player)) {
            if (health >= 0 && health <= player.maxHealth) {
                player.health = health
            }
        }
    }

    fun addHeartDamageReduction(event: EntityDamageByEntityEvent, hearts: Int) {
        val data = eventData.computeIfAbsent(event.damager.uniqueId) { k: UUID? -> EventData() }
        data.heartReductionAmount = data.heartReductionAmount + hearts
    }

    private class EventData {
        var additiveDamage = 1.0
        var multiplicativeDamage = 1.0
        var reductionAmount = 1.0
        var heartReductionAmount = 0
    }
}