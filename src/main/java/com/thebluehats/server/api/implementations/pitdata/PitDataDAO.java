package com.thebluehats.server.api.implementations.pitdata;

import org.bukkit.entity.Player;

public interface PitDataDAO {
    int getPlayerXp(Player player);

    void setPlayerXp(Player player, int value);

    double getPlayerGold(Player player);

    void setPlayerGold(Player player, double value);
}
