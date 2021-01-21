package com.thebluehats.server.game.enchants

import com.google.common.util.concurrent.AtomicDouble
import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.enchants.CustomEnchant
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.*
import org.bukkit.entity.Chicken
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class DevilChicks @Inject constructor(
    private val plugin: JavaPlugin,
    private val damageManager: DamageManager,
    private val customEnchantUtils: CustomEnchantUtils
) : CustomEnchant, Listener {
    private val amountOfChicks = EnchantProperty(1, 2, 3)
    private val devilchickAnimations = HashMap<UUID, Int>()
    @EventHandler
    fun onArrowLand(event: ProjectileHitEvent) {
        val projectile = event.entity
        if (projectile.shooter is Player) {
            val shooter = event.entity.shooter as Player
            val bow = shooter.inventory.itemInHand
            if (customEnchantUtils.itemHasEnchant(this, bow)) {
                execute(customEnchantUtils.getEnchantLevel(this, bow), shooter, projectile)
            }
        }
    }

    // TODO Add getValidators to CustomEnchant interface
    // TODO Fix dead-alive bug
    fun execute(level: Int, shooter: Player, arrow: Projectile) {
        val world = shooter.world
        val arrowUuid = UUID.randomUUID()
        val shooterLocation = shooter.location
        val arrowLocation = arrow.location
        val chickenAmount = amountOfChicks.getValueAtLevel(level)
        val pitchIncrement = 0.1
        val volume = 0.5f
        val blastRadius = 0.75f
        val pitch = AtomicDouble(0.6)
        val animationIndex = AtomicInteger()
        val chickens = arrayOfNulls<Chicken>(chickenAmount)
        for (i in chickens.indices) {
            val direction = Vector()
            direction.x = arrowLocation.x + (Math.random() - Math.random()) * blastRadius
            direction.y = arrowLocation.y
            direction.z = arrowLocation.z + (Math.random() - Math.random()) * blastRadius
            val chicken = world.spawnEntity(direction.toLocation(arrowLocation.world), EntityType.CHICKEN) as Chicken
            chicken.setBaby()
            chickens[i] = chicken
        }
        val taskId = Bukkit.getServer().scheduler.scheduleSyncRepeatingTask(plugin, {
            if (animationIndex.get() == 10) {
                Bukkit.getServer().scheduler.cancelTask(devilchickAnimations[arrowUuid]!!)
                devilchickAnimations.remove(arrowUuid)
                world.playSound(shooterLocation, Sound.CHICKEN_HURT, 1f, 2f)
                for (chicken in chickens) {
                    for (entity in chicken!!.getNearbyEntities(1.0, 1.0, 1.0)) {
                        if (entity is Player) {
                            val player = entity
                            damageManager.doTrueDamage(player, 2.4, shooter)
                            createExplosion(player, chicken.location)
                        }
                    }
                    world.playSound(arrowLocation, Sound.EXPLODE, volume, 1.6f)
                    world.playEffect(chicken.location, Effect.EXPLOSION_LARGE, Effect.EXPLOSION_LARGE.data, 100)
                    chicken.remove()
                }
            }
            world.playSound(shooterLocation, Sound.NOTE_SNARE_DRUM, volume, pitch.get().toFloat())
            pitch.addAndGet(pitchIncrement)
            animationIndex.incrementAndGet()
        }, 0L, 1L)
        devilchickAnimations[arrowUuid] = taskId
    }

    private fun createExplosion(target: Player, position: Location) {
        val explosion = target.location.toVector().subtract(position.toVector()).normalize()
        target.velocity = explosion
    }

    override fun getName(): String {
        return "Devil Chicks"
    }

    override fun getEnchantReferenceName(): String {
        return "Devilchicks"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Arrows ")
        enchantLoreParser.addTextIf(level == 1, "spawn with explosive chicken.")
        enchantLoreParser.addTextIf(level == 2, "spawn many explosive chickens.")
        enchantLoreParser.addTextIf(level == 3, "spawn too many explosive chickens.")
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