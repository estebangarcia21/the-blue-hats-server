package com.thebluehats.server.api.models;

import java.util.UUID;

public class PitDataModel {
    private final UUID playerUuid;

    private int prestige;
    private int level;
    private int xp;
    private float gold;

    public PitDataModel(UUID playeruUuid, int prestige, int level, int xp, float gold) {
        this.playerUuid = playeruUuid;
        this.prestige = prestige;
        this.level = level;
        this.xp = xp;
        this.gold = gold;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public int getPrestige() {
        return prestige;
    }

    public void setPrestige(int value) {
        this.prestige = value;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int value) {
        this.xp = value;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int value) {
        this.level = value;
    }

    public float getGold() {
        return gold;
    }

    public void setGold(float value) {
        this.gold = value;
    }
}
