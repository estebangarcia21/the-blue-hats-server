package com.thebluehats.server.api.repos;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

import com.thebluehats.server.api.models.PitDataModel;

public class PitDataRepository implements Repository<UUID, PitDataModel> {
    private final ArrayList<PitDataModel> models = new ArrayList<>();

    @Override
    public void add(PitDataModel model) {
        models.add(model);
    }

    @Override
    public PitDataModel findUnique(UUID key) {
        for (PitDataModel model : models) {
            if (model.getPlayerUuid() == key) {
                return model;
            }
        }

        return null;
    }

    @Override
    public void delete(UUID key) {
        PitDataModel model = findUnique(key);

        models.remove(model);
    }

    @Override
    public void update(UUID key, Consumer<PitDataModel> update) {
        PitDataModel model = findUnique(key);

        update.accept(model);
    }
}
