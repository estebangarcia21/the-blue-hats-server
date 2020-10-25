package me.stevemmmmm.server.game.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

public class DamageManager {
    private static DamageManager instance;

    private final HashMap<UUID, DamageEventData> eventData = new HashMap<>();
    private final ArrayList<UUID> canceledPlayers = new ArrayList<>();

    public static DamageManager getInstance() {
        if (instance == null)
            instance = new DamageManager();

        return instance;
    }

    public boolean playerIsInCanceledEvent(Player player) {
        return canceledPlayers.contains(player.getUniqueId());

    }

    public boolean arrowIsInCanceledEvent(Arrow arrow) {
        if (arrow.getShooter() instanceof Player)
            return canceledPlayers.contains(((Player) arrow.getShooter()).getUniqueId());

        return false;
    }
}
