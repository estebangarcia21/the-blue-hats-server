package com.thebluehats.server.api.listeners

import com.thebluehats.server.core.modules.annotations.ServerAPI
import kong.unirest.UnirestInstance
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import javax.inject.Inject

class RegisterPlayer @Inject constructor(@param:ServerAPI private val serverAPI: UnirestInstance) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        serverAPI.post("/api/v1/players")
            .header("Content-Type", "application/json")
            .body("""{ "uuid": ${event.player.uniqueId}} """)
            .asJson()
    }
}
