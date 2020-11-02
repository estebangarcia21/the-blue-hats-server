package com.thebluehats.server.game.perks;

import org.bukkit.event.Listener;

public abstract class Perk implements Listener {
    public abstract String getName();

    public abstract int getCost();
}
