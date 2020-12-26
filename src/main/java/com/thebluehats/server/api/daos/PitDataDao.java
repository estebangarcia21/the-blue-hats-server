package com.thebluehats.server.api.daos;

import org.bukkit.entity.Player;

public interface PitDataDao {
    int getPlayerXp(Player player);

    void setPlayerXp(Player player, int value);

    double getPlayerGold(Player player);

    void setPlayerGold(Player player, double value);
}
