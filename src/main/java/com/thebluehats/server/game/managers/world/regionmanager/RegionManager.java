package com.thebluehats.server.game.managers.world.regionmanager;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.thebluehats.server.game.managers.world.regionmanager.maps.response.Position;
import com.thebluehats.server.game.managers.world.regionmanager.maps.response.Bounds;
import com.thebluehats.server.game.managers.world.regionmanager.maps.response.Map;
import com.thebluehats.server.game.managers.world.regionmanager.maps.response.Spawn;
import com.thebluehats.server.game.utils.EntityValidator;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

public class RegionManager implements EntityValidator {
    private final ArrayList<Map> maps = new ArrayList<>();

    private final String ACTIVE_MAP_NAME = "Genesis";
    private final Map activeMap;

    public RegionManager() {
        parseMapsFromJSON();

        activeMap = maps.stream().filter(map -> map.getName().equalsIgnoreCase(ACTIVE_MAP_NAME)).findFirst().get();
    }

    private void parseMapsFromJSON() {
        Gson gson = new Gson();

        Reader reader = new InputStreamReader(getClass().getResourceAsStream("/maps.json"));

        maps.addAll(Arrays.asList(gson.fromJson(reader, Map[].class)));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getEntity() instanceof Player) {
            Player hit = (Player) event.getEntity();

            if (entityIsInSpawn(hit)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        if (event.getProjectile() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getProjectile();

            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();

                if (entityIsInSpawn(player)) {
                    event.setCancelled(true);

                    (player).updateInventory();
                }
            }
        }
    }

    public Location getSpawnLocation(Player player) {
        Spawn spawn = activeMap.getSpawn();

        Position spawnLocation = spawn.getLocation();
        float rotation = spawn.getRotation();

        return new Location(player.getWorld(), spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(),
                rotation, 0);
    }

    public boolean entityIsInPlayableArea(Entity entity) {
        Bounds mapBounds = activeMap.getBounds();

        Vector lowerBound = vectorFromBound(mapBounds.getMinBound());
        Vector higherBound = vectorFromBound(mapBounds.getMaxBound());

        return isInBounds(entity.getLocation(), lowerBound, higherBound);
    }

    public boolean entityIsInSpawn(Entity entity) {
        return locationisInSpawn(entity.getLocation());
    }

    public boolean locationisInSpawn(Location location) {
        Bounds spawnBounds = activeMap.getSpawn().getBounds();

        Vector lowerBound = vectorFromBound(spawnBounds.getMinBound());
        Vector higherBound = vectorFromBound(spawnBounds.getMaxBound());

        return isInBounds(location, lowerBound, higherBound);
    }

    private boolean isInBounds(Location location, Vector lowerBound, Vector higherBound) {
        return location.getY() > lowerBound.getY() && location.getY() < higherBound.getY()
                && location.getX() < lowerBound.getX() && location.getX() > higherBound.getX()
                && location.getZ() < lowerBound.getZ() && location.getZ() > higherBound.getZ();
    }

    private Vector vectorFromBound(Position bound) {
        return new Vector(bound.getX(), bound.getY(), bound.getZ());
    }

    @Override
    public boolean validate(Entity... entities) {
        for (Entity entity : entities) {
            if (entityIsInSpawn(entity)) {
                return false;
            }
        }

        return true;
    }
}
