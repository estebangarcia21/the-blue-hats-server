package com.thebluehats.server.game.managers.world.regionmanager.maps.response;

public class Spawn {
    private final Position location;
    private final float rotation;
    private final Bounds bounds;

    public Spawn(Position location, float rotation, Bounds bounds) {
        this.location = location;
        this.rotation = rotation;
        this.bounds = bounds;
    }

    public Position getLocation() {
        return location;
    }

    public float getRotation() {
        return rotation;
    }

    public Bounds getBounds() {
        return bounds;
    }
}
