package com.thebluehats.server.api.implementations.pitdata;

import java.util.UUID;

import com.thebluehats.server.api.models.PitDataModel;
import com.thebluehats.server.api.utils.CRUDRepository;

public class PitDataRepository extends CRUDRepository<PitDataModel, UUID> {
    public PitDataRepository() {
        super(PitDataModel::getPlayerUuid);
    }
}
