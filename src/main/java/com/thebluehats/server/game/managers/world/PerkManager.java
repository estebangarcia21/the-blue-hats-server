package com.thebluehats.server.game.managers.world;

import java.util.ArrayList;
import java.util.Arrays;

import com.thebluehats.server.game.perks.Perk;
import com.thebluehats.server.game.utils.Registerer;

public class PerkManager implements Registerer<Perk> {
    private final ArrayList<Perk> perks = new ArrayList<>();

    @Override
    public void register(Perk[] perks) {
        this.perks.addAll(Arrays.asList(perks));
    }
}
