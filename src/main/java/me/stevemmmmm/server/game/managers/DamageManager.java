package me.stevemmmmm.server.game.managers;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

public class DamageManager {
    private static DamageManager instance;

    private final HashMap<UUID, DamageEventData> eventData = new HashMap<>();

    private DamageManager() {
    }

    public static DamageManager getInstance() {
        if (instance == null)
            instance = new DamageManager();

        return instance;
    }

    public boolean playerIsInCanceledEvent(Player player) {
        // TODO Implmenent
        return true;
    }

    public boolean arrowIsInCanceledEvent(Arrow arrow) {
        // TODO Implement
        return true;
    }
}
