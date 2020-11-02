package com.thebluehats.server.game.managers.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

public class RegionManager {
    private static RegionManager instance;

    public final static Map GAME_MAP = Map.SEASONS;

    private final ArrayList<Region> regions = new ArrayList<>();
    private final HashMap<Map, Location> spawnLocations = new HashMap<>();

    private RegionManager() {
        initSpawnRegions();
    }

    public static RegionManager getInstance() {
        if (instance == null)
            instance = new RegionManager();

        return instance;
    }

    private void initSpawnRegions() {
        // Genisis Spawn
        spawnLocations.put(Map.GENISIS, new Location(null, 0.5, 86.5, 11.5, -180, 0));

        // Seasons Spawn
        spawnLocations.put(Map.SEASONS, new Location(null, 0.5, 114.5, 9.5, -180, 0));

        // Genisis Map
        regions.add(
                new Region(Map.GENISIS, new Vector(35.5, 77.5, 30), new Vector(-42.5, 111.5, -45.5), RegionType.SPAWN));
        regions.add(new Region(Map.GENISIS, new Vector(120.5, 0, 144.5), new Vector(-126.5, 129.5, -124.607),
                RegionType.PLAYABLEAREA));

        // Seasons Map
        regions.add(new Region(Map.SEASONS, new Vector(35.5, 107.5, 30), new Vector(-42.5, 141.5, -45.5),
                RegionType.SPAWN));
        regions.add(new Region(Map.SEASONS, new Vector(130.5, 0, 144.5), new Vector(-126.5, 150, -220),
                RegionType.PLAYABLEAREA));
    }

    public Location getSpawnLocation(Player player) {
        return new Location(player.getWorld(), spawnLocations.get(GAME_MAP).getX(), spawnLocations.get(GAME_MAP).getY(),
                spawnLocations.get(GAME_MAP).getZ(), spawnLocations.get(GAME_MAP).getYaw(),
                spawnLocations.get(GAME_MAP).getPitch());
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getEntity() instanceof Player) {
            Player hit = (Player) event.getEntity();

            if (playerIsInRegion(hit, RegionType.SPAWN)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        if (event.getProjectile() instanceof Arrow) {
            if (((Arrow) event.getProjectile()).getShooter() instanceof Player) {
                if (playerIsInRegion(((Player) ((Arrow) event.getProjectile()).getShooter()), RegionType.SPAWN)) {
                    event.setCancelled(true);
                    ((Player) ((Arrow) event.getProjectile()).getShooter()).updateInventory();
                }
            }
        }
    }

    public boolean playerIsInRegion(Player player, RegionType regionType) {
        Location location = player.getLocation();

        for (Region region : regions) {
            if (region.regionType == regionType && region.map == GAME_MAP) {
                if (location.getY() > region.lowerBound.getY() && location.getY() < region.higherBound.getY()
                        && location.getX() < region.lowerBound.getX() && location.getX() > region.higherBound.getX()
                        && location.getZ() < region.lowerBound.getZ() && location.getZ() > region.higherBound.getZ()) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean locationIsInRegion(Location location, RegionType regionType) {
        for (Region region : regions) {
            if (region.regionType == regionType && region.map == GAME_MAP) {
                if (location.getY() > region.lowerBound.getY() && location.getY() < region.higherBound.getY()
                        && location.getX() < region.lowerBound.getX() && location.getX() > region.higherBound.getX()
                        && location.getZ() < region.lowerBound.getZ() && location.getZ() > region.higherBound.getZ()) {
                    return true;
                }
            }
        }

        return false;
    }

    static class Region {
        private final Map map;

        private final Vector lowerBound;
        private final Vector higherBound;
        private final RegionType regionType;

        public Region(Map map, Vector lowerBound, Vector higherBound, RegionType regionType) {
            this.map = map;
            this.higherBound = higherBound;
            this.lowerBound = lowerBound;
            this.regionType = regionType;
        }
    }

    public enum RegionType {
        SPAWN, PLAYABLEAREA
    }

    public enum Map {
        GENISIS, SEASONS
    }
}
