package com.thebluehats.server.game.managers.game.regionmanager.maps.response;

public class Bounds {
    private final Bound minBound;
    private final Bound maxBound;

    public Bounds(Bound minBound, Bound maxBound) {
        this.minBound = minBound;
        this.maxBound = maxBound;
    }

    public Bound getMinBound() {
        return minBound;
    }

    public Bound getMaxBound() {
        return maxBound;
    }
}
