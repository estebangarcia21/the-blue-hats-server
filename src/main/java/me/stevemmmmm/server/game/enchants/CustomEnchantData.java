package me.stevemmmmm.server.game.enchants;

public class CustomEnchantData {
    private long cooldownTime;
    private int cooldownTaskId;

    private int hitsWithEnchant;
    private long hitResetTime;
    private int hitResetTaskId;

    public CustomEnchantData() {
        this.cooldownTime = 0;
        this.cooldownTaskId = 0;
        this.hitsWithEnchant = 0;
        this.hitResetTime = 0;
        this.hitResetTaskId = 0;
    }

    public CustomEnchantData(long cooldownTime, int cooldownTaskId, int hitsWithEnchant, long hitResetTime,
            int hitResetTaskId) {
        this.cooldownTime = cooldownTime;
        this.cooldownTaskId = cooldownTaskId;
        this.hitsWithEnchant = hitsWithEnchant;
        this.hitResetTime = hitResetTime;
        this.hitResetTaskId = hitResetTaskId;
    }

    public boolean isOnCooldown() {
        return getCooldownTime() != 0;
    }

    public long getCooldownTime() {
        return cooldownTime;
    }

    public void setCooldownTime(long value) {
        cooldownTime = value;
    }

    public int getCooldownTaskId() {
        return cooldownTaskId;
    }

    public void setCooldownTaskId(int value) {
        cooldownTaskId = value;
    }

    public int getHitsWithEnchant() {
        return hitsWithEnchant;
    }

    public void setHitsWithEnchant(int value) {
        hitsWithEnchant = value;
    }

    public long getHitResetTime() {
        return hitResetTime;
    }

    public void setHitResetTime(long value) {
        hitResetTime = value;
    }

    public int getHitResetTaskId() {
        return hitResetTaskId;
    }

    public void setHitResetTaskId(int value) {
        hitResetTaskId = value;
    }
}
