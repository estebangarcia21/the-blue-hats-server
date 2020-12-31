package com.thebluehats.server.game.world;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ClearArrows implements Listener {
    private final HashMap<UUID, ArrayList<Arrow>> playerToArrows = new HashMap<>();

    private final JavaPlugin plugin;

    @Inject
    public ClearArrows(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProjectileShoot(EntityShootBowEvent event) {
        Entity projectile = event.getProjectile();

        if (event.getProjectile() instanceof Arrow) {
            if (((Arrow) event.getProjectile()).getShooter() instanceof Player) {
                Player player = (Player) ((Arrow) event.getProjectile()).getShooter();

                if (!playerToArrows.containsKey(player.getUniqueId())) {
                    playerToArrows.put(player.getUniqueId(), new ArrayList<>());
                }

                ArrayList<Arrow> data = playerToArrows.get(player.getUniqueId());

                // TODO Server config
                int ARROW_MAXIMUM = 7;

                if (data.size() >= ARROW_MAXIMUM) {
                    data.get(0).remove();
                    data.remove(0);
                }

                data.add((Arrow) event.getProjectile());

                playerToArrows.put(player.getUniqueId(), data);
            }
        }

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (projectile != null) {
                projectile.remove();
            }
        }, 100L);
    }
}