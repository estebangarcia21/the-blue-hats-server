package com.thebluehats.server.game.managers.world.regionmanager

import com.google.gson.Gson
import com.thebluehats.server.game.managers.world.regionmanager.maps.response.Map
import com.thebluehats.server.game.managers.world.regionmanager.maps.response.Position
import com.thebluehats.server.game.utils.EntityValidator
import org.bukkit.Location
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.util.Vector
import java.io.InputStreamReader
import java.io.Reader
import java.util.*

class RegionManager : EntityValidator {
    private val maps = ArrayList<Map>()
    private val activeMapName = "Genesis"
    private val activeMap: Map

    private fun parseMapsFromJSON() {
        val gson = Gson()
        val reader: Reader = InputStreamReader(javaClass.getResourceAsStream("/maps.json"))
        maps.addAll(listOf(*gson.fromJson(reader, Array<Map>::class.java)))
    }

    @EventHandler
    fun onHit(event: EntityDamageByEntityEvent) {
        if (event.entity is Player && event.entity is Player) {
            val hit = event.entity as Player
            if (entityIsInSpawn(hit)) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onArrowShoot(event: EntityShootBowEvent) {
        if (event.projectile is Arrow) {
            val arrow = event.projectile as Arrow
            if (arrow.shooter is Player) {
                val player = arrow.shooter as Player
                if (entityIsInSpawn(player)) {
                    event.isCancelled = true
                    player.updateInventory()
                }
            }
        }
    }

    fun getSpawnLocation(player: Player): Location {
        val spawn = activeMap.spawn
        val spawnLocation = spawn.location
        val rotation = spawn.rotation

        return Location(
            player.world, spawnLocation.x.toDouble(), spawnLocation.y.toDouble(), spawnLocation.z.toDouble(),
            rotation, 0F
        )
    }

    fun entityIsInPlayableArea(entity: Entity): Boolean {
        val mapBounds = activeMap.bounds
        val lowerBound = vectorFromBound(mapBounds.minBound)
        val higherBound = vectorFromBound(mapBounds.maxBound)
        return isInBounds(entity.location, lowerBound, higherBound)
    }

    fun entityIsInSpawn(entity: Entity): Boolean {
        return locationisInSpawn(entity.location)
    }

    fun locationisInSpawn(location: Location): Boolean {
        val spawnBounds = activeMap.spawn.bounds
        val lowerBound = vectorFromBound(spawnBounds.minBound)
        val higherBound = vectorFromBound(spawnBounds.maxBound)
        return isInBounds(location, lowerBound, higherBound)
    }

    private fun isInBounds(location: Location, lowerBound: Vector, higherBound: Vector): Boolean {
        return location.y > lowerBound.y && location.y < higherBound.y && location.x < lowerBound.x && location.x > higherBound.x && location.z < lowerBound.z && location.z > higherBound.z
    }

    private fun vectorFromBound(bound: Position): Vector {
        return Vector(bound.x, bound.y, bound.z)
    }

    override fun validate(vararg entities: Array<Entity>): Boolean {
        for (entity in entities) {
            if (entityIsInSpawn(entity)) {
                return false
            }
        }

        return true
    }

    init {
        parseMapsFromJSON()
        activeMap = maps.stream().filter { map: Map -> map.name.equals(activeMapName, ignoreCase = true) }
            .findFirst().get()
    }
}