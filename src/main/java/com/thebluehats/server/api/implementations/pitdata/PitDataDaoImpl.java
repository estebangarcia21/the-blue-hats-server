package com.thebluehats.server.api.implementations.pitdata;

import com.google.inject.Inject;
import com.thebluehats.server.api.daos.PitDataDao;
import com.thebluehats.server.api.models.PitDataModel;
import com.thebluehats.server.api.utils.CrudRepository;
import com.thebluehats.server.api.utils.DataLoader;
import com.thebluehats.server.core.modules.annotations.PitDataProvider;
import com.thebluehats.server.core.modules.annotations.ServerAPI;
import com.thebluehats.server.game.utils.PluginLifecycleListener;
import kong.unirest.UnirestInstance;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PitDataDaoImpl implements PitDataDao, PluginLifecycleListener, DataLoader {
    private final CrudRepository<PitDataModel, UUID> pitDataRepository;
    private final UnirestInstance serverAPI;

    @Inject
    public PitDataDaoImpl(@ServerAPI UnirestInstance serverAPI,
                          @PitDataProvider CrudRepository<PitDataModel, UUID> pitDataRepository) {
        this.pitDataRepository = pitDataRepository;
        this.serverAPI = serverAPI;
    }

    @Override
    public int getPlayerXp(Player player) {
        // TODO Next level xp
        return serverAPI.get("/api/v1/game-data/the-pit/{uuid}")
            .routeParam("uuid", player.getUniqueId().toString())
            .queryString("key", System.getenv("API_KEY"))
            .asJson().getBody().getObject().getInt("xp");
    }

    @Override
    public void setPlayerXp(Player player, int value) {
        pitDataRepository.update(player.getUniqueId(), model -> model.setXp(value));
    }

    @Override
    public double getPlayerGold(Player player) {
        return serverAPI.get("/api/v1/game-data/the-pit/{uuid}")
            .routeParam("uuid", player.getUniqueId().toString())
            .queryString("key", System.getenv("API_KEY"))
            .asJson().getBody().getObject().getDouble("gold");
    }

    @Override
    public void setPlayerGold(Player player, double value) {
//        serverApi.put("/game-data/the-pit/{uuid}")
//            .routeParam("uuid", player.getUniqueId().toString())
//            .queryString("key", API_KEY)
//            .header("", "")
//            .header("Content-Type", "application/json")
//            .body("{\n    \"type\": \"performance\",\n    \"data\": {\n        \"xp\": " + value + "\n    }\n}");
    }

    @Override
    public void onPluginStart() {
        loadData();
    }

    @Override
    public void onPluginEnd() {
        saveData();
    }

    @Override
    public void loadData() {
        // TODO GET from API
    }

    @Override
    public void saveData() {
        // TODO POST from API
    }
}
