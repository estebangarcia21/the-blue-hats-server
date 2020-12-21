package com.thebluehats.server.game.managers.game.regionmanager;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.thebluehats.server.game.managers.game.regionmanager.maps.response.Map;
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

    public RegionManager() {
        parseMapsFromJSON();
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

        // for (Region region : regions) {
        // if (region.regionType == regionType && region.map == GAME_MAP) {
        // if (location.getY() > region.lowerBound.getY() && location.getY() <
        // region.higherBound.getY()
        // && location.getX() < region.lowerBound.getX() && location.getX() >
        // region.higherBound.getX()
        // && location.getZ() < region.lowerBound.getZ() && location.getZ() >
        // region.higherBound.getZ()) {
        // return true;
        // }
        // }
        // }

        return false;
    }

    public boolean locationIsInRegion(Location location, RegionType regionType) {
        // for (Region region : regions) {
        // if (region.regionType == regionType && region.map == GAME_MAP) {
        // if (location.getY() > region.lowerBound.getY() && location.getY() <
        // region.higherBound.getY()
        // && location.getX() < region.lowerBound.getX() && location.getX() >
        // region.higherBound.getX()
        // && location.getZ() < region.lowerBound.getZ() && location.getZ() >
        // region.higherBound.getZ()) {
        // return true;
        // }
        // }
        // }

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

    public Location getSpawnLocation(Player player) {
        return null;
    }

    @Override
    public boolean validate(Entity... entities) {
        // TODO Validate
        return true;
    }
}
