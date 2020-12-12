package com.thebluehats.server.game.managers.game.regionmanager;

import org.bukkit.util.Vector;

public class Map {
    private final String name;

    private final Vector spawnLocation;
    private final float spawnRotation;

    private final Vector minBound;
    private final Vector maxBound;

    public Map(String name, Vector spawnLocation, float spawnRotation, Vector minBound, Vector maxBound) {
        this.name = name;
        this.spawnLocation = spawnLocation;
        this.spawnRotation = spawnRotation;
        this.minBound = minBound;
        this.maxBound = maxBound;
    }

    public String getName() {
        return name;
    }

    public Vector getSpawnLocation() {
        return spawnLocation;
    }

    public float getSpawnRotation() {
        return spawnRotation;
    }

    public Vector getMinBound() {
        return minBound;
    }

    public Vector getMaxBound() {
        return maxBound;
    }
}
