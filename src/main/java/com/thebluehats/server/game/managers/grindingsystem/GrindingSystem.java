package com.thebluehats.server.game.managers.grindingsystem;

import java.util.UUID;

import com.google.inject.Inject;
import com.thebluehats.server.api.models.PitDataModel;
import com.thebluehats.server.api.repos.Repository;
import com.thebluehats.server.api.utils.DataLoader;
import com.thebluehats.server.core.modules.annotations.ServerApi;
import com.thebluehats.server.game.utils.PluginLifecycle;

import kong.unirest.UnirestInstance;

public class GrindingSystem implements PluginLifecycle, DataLoader {
    private final Repository<UUID, PitDataModel> pitDataRepository;
    private final UnirestInstance serverApi;

    @Inject
    public GrindingSystem(@ServerApi UnirestInstance serverApi, Repository<UUID, PitDataModel> pitDataRepository) {
        this.pitDataRepository = pitDataRepository;
        this.serverApi = serverApi;
    }

    public void setPlayerXp(UUID player, int value) {
        pitDataRepository.update(player, model -> model.setXp(value));
    }

    public int getPlayerXp(UUID player) {
        return pitDataRepository.findUnique(player).getXp();
    }

    public void getPlayerGold(UUID player, float value) {
        pitDataRepository.update(player, model -> model.setGold(value));
    }

    public void setPlayerGold(UUID player, float value) {
        pitDataRepository.update(player, model -> model.setGold(value));
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
        serverApi.get("/players/");
    }

    @Override
    public void saveData() {
        // TODO POST from API
        serverApi.get("/players/");
    }
}
