package com.thebluehats.server.api.repos;

import java.util.UUID;

import com.thebluehats.server.api.models.PitDataModel;

public class PitDataRepository extends CRUDRepository<PitDataModel, UUID> {
    public PitDataRepository() {
        super(model -> model.getPlayerUuid());
    }
}
