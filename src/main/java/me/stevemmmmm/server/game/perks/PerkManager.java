package me.stevemmmmm.server.game.perks;

import java.util.ArrayList;

public class PerkManager {
    private ArrayList<Perk> perks = new ArrayList<>();

    public void registerPerk(Perk perk) {
        perks.add(perk);
    }
}
