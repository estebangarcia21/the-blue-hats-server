package com.thebluehats.server.game.managers.world;

import com.thebluehats.server.game.perks.Perk;
import com.thebluehats.server.game.utils.Registerer;

import java.util.ArrayList;
import java.util.Arrays;

public class PerkManager implements Registerer<Perk> {
    private final ArrayList<Perk> perks = new ArrayList<>();

    @Override
    public void register(Perk... perks) {
        this.perks.addAll(Arrays.asList(perks));
    }
}
