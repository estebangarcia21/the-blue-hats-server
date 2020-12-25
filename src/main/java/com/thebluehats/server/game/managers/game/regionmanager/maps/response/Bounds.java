package com.thebluehats.server.game.managers.game.regionmanager.maps.response;

public class Bounds {
    private final Position minBound;
    private final Position maxBound;

    public Bounds(Position minBound, Position maxBound) {
        this.minBound = minBound;
        this.maxBound = maxBound;
    }

    public Position getMinBound() {
        return minBound;
    }

    public Position getMaxBound() {
        return maxBound;
    }
}
