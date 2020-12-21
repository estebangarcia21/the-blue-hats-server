package com.thebluehats.server.game.managers.game.regionmanager.maps.response;

public class Map {
    private final String name;
    private final Spawn spawn;
    private final Bounds bounds;

    public Map(String name, Spawn spawn, Bounds bounds) {
        this.name = name;
        this.spawn = spawn;
        this.bounds = bounds;
    }

    public String getName() {
        return name;
    }

    public Spawn getSpawn() {
        return spawn;
    }

    public Bounds getBounds() {
        return bounds;
    }
}
