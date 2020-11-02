package com.thebluehats.server.game.managers.enchants;

import java.util.concurrent.ThreadLocalRandom;

public class RandomPercentage {
    public boolean percentChance(int percent) {
        return ThreadLocalRandom.current().nextInt(0, 100) <= percent;
    }
}
