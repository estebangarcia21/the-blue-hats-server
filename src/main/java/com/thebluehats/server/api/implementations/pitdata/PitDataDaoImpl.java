package com.thebluehats.server.api.implementations.pitdata;

import java.util.UUID;

import com.google.inject.Inject;
import com.thebluehats.server.api.daos.PitDataDao;
import com.thebluehats.server.api.models.PitDataModel;
import com.thebluehats.server.api.utils.CrudRepository;
import com.thebluehats.server.api.utils.DataLoader;
import com.thebluehats.server.core.modules.annotations.PitDataProvider;
import com.thebluehats.server.core.modules.annotations.ServerApi;
import com.thebluehats.server.game.utils.PluginLifecycleListener;

import org.bukkit.entity.Player;

import kong.unirest.UnirestInstance;

public class PitDataDaoImpl implements PitDataDao, PluginLifecycleListener, DataLoader {
    private final CrudRepository<PitDataModel, UUID> pitDataRepository;
    private final UnirestInstance serverApi;

    @Inject
    public PitDataDaoImpl(@ServerApi UnirestInstance serverApi,
            @PitDataProvider CrudRepository<PitDataModel, UUID> pitDataRepository) {
        this.pitDataRepository = pitDataRepository;
        this.serverApi = serverApi;
    }

    @Override
    public int getPlayerXp(Player player) {
        return pitDataRepository.findUnique(player.getUniqueId()).getXp();
    }

    @Override
    public void setPlayerXp(Player player, int value) {
        pitDataRepository.update(player.getUniqueId(), model -> model.setXp(value));
    }

    @Override
    public double getPlayerGold(Player player) {
        return pitDataRepository.findUnique(player.getUniqueId()).getGold();
    }

    @Override
    public void setPlayerGold(Player player, double value) {
        pitDataRepository.update(player.getUniqueId(), model -> model.setGold(value));
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