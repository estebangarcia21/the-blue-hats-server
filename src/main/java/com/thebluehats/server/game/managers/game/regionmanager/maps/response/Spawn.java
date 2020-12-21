package com.thebluehats.server.game.managers.game.regionmanager.maps.response;

public class Spawn {
    private final Bound location;
    private final float rotation;
    private final Bounds bounds;

    public Spawn(Bound location, float rotation, Bounds bounds) {
        this.location = location;
        this.rotation = rotation;
        this.bounds = bounds;
    }

    public Bound getLocation() {
        return location;
    }

    public float getRotation() {
        return rotation;
    }

    public Bounds getBounds() {
        return bounds;
    }
}
