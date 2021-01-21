package com.thebluehats.server.api.implementations.pitdata

import com.google.inject.Inject
import com.thebluehats.server.api.daos.PerformanceStatsService
import com.thebluehats.server.core.modules.annotations.ServerAPI
import kong.unirest.UnirestInstance
import org.bukkit.entity.Player

class PerformanceStatsServiceImpl @Inject constructor(@param:ServerAPI private val serverAPI: UnirestInstance) :
    PerformanceStatsService {
    override fun getPlayerXp(player: Player): Int {
        return serverAPI["/api/v1/game-data/the-pit/{uuid}"]
            .routeParam("uuid", player.uniqueId.toString())
            .asJson().body.getObject().getInt("xp")
    }

    override fun setPlayerXp(player: Player, value: Int) {
        serverAPI.put("/game-data/the-pit/{uuid}")
            .routeParam("uuid", player.uniqueId.toString())
            .header("Content-Type", "application/json")
            .body("{\n    \"type\": \"performance\",\n    \"data\": {\n        \"xp\": $value\n    }\n}")
            .asJson()
    }

    override fun getPlayerGold(player: Player): Double {
        return serverAPI["/api/v1/game-data/the-pit/{uuid}"]
            .routeParam("uuid", player.uniqueId.toString())
            .asJson().body.getObject().getDouble("gold")
    }

    override fun setPlayerGold(player: Player, value: Double) {
        serverAPI.put("/api/v1/game-data/the-pit/{uuid}")
            .routeParam("uuid", player.uniqueId.toString())
            .header("Content-Type", "application/json")
            .body("{\n    \"gold\": $value\n}")
            .asJson()
    }
}