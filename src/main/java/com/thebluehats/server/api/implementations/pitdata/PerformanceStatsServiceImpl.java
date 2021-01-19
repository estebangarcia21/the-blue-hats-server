package com.thebluehats.server.api.implementations.pitdata;

import com.google.inject.Inject;
import com.thebluehats.server.api.daos.PerformanceStatsService;
import com.thebluehats.server.core.modules.annotations.ServerAPI;
import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;
import org.bukkit.entity.Player;

public class PerformanceStatsServiceImpl implements PerformanceStatsService {
    private final UnirestInstance serverAPI;

    @Inject
    public PerformanceStatsServiceImpl(@ServerAPI UnirestInstance serverAPI) {
        this.serverAPI = serverAPI;
    }

    @Override
    public int getPlayerXp(Player player) {
        return serverAPI.get("/api/v1/game-data/the-pit/{uuid}")
            .routeParam("uuid", player.getUniqueId().toString())
            .asJson().getBody().getObject().getInt("xp");
    }

    @Override
    public void setPlayerXp(Player player, int value) {
        serverAPI.put("/game-data/the-pit/{uuid}")
            .routeParam("uuid", player.getUniqueId().toString())
            .header("Content-Type", "application/json")
            .body("{\n    \"type\": \"performance\",\n    \"data\": {\n        \"xp\": " + value + "\n    }\n}")
            .asJson();
    }

    @Override
    public double getPlayerGold(Player player) {
        return serverAPI.get("/api/v1/game-data/the-pit/{uuid}")
            .routeParam("uuid", player.getUniqueId().toString())
            .asJson().getBody().getObject().getDouble("gold");
    }

    @Override
    public void setPlayerGold(Player player, double value) {
        serverAPI.put("/api/v1/game-data/the-pit/{uuid}")
            .routeParam("uuid", player.getUniqueId().toString())
            .header("Content-Type", "application/json")
            .body("{\n    \"gold\": " + value + "\n}")
            .asJson();
    }
}
