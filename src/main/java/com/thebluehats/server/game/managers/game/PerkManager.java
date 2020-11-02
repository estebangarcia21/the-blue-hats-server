package com.thebluehats.server.game.managers.game;

import com.thebluehats.server.game.perks.Perk;

import java.util.ArrayList;

public class PerkManager {
    private ArrayList<Perk> perks = new ArrayList<>();

    public void registerPerk(Perk perk) {
        perks.add(perk);
    }
}
