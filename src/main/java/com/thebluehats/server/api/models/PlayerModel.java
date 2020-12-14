package com.thebluehats.server.api.models;

import java.util.UUID;

public class PlayerModel {
    private final UUID uuid;
    private final PitDataModel pitDataModel;

    private Rank rank;

    public PlayerModel(UUID uuid, Rank rank, PitDataModel pitDataModel) {
        this.uuid = uuid;
        this.rank = rank;
        this.pitDataModel = pitDataModel;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank value) {
        this.rank = value;
    }

    public PitDataModel getPitDataModel() {
        return pitDataModel;
    }
}
