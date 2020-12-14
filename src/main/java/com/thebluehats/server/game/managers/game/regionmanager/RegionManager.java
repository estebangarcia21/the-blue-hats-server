package com.thebluehats.server.game.managers.game.regionmanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.thebluehats.server.game.utils.EntityValidator;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RegionManager implements EntityValidator {
    private static RegionManager instance;

    private final ArrayList<Region> regions = new ArrayList<>();
    private final ArrayList<Map> maps = new ArrayList<>();

    private RegionManager() {
        parseMapsFromXmlConfig();
    }

    public static RegionManager getInstance() {
        if (instance == null)
            instance = new RegionManager();

        return instance;
    }

    private void parseMapsFromXmlConfig() {
        DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder xmlBuilder = xmlFactory.newDocumentBuilder();
            Document parseResult = xmlBuilder
                    .parse(new File("src/main/java/com/thebluehats/server/game/managers/game/regionmanager/maps.xml"));

            NodeList map = parseResult.getElementsByTagName("map");

            for (int i = 0; i < map.getLength(); i++) {
                Element currentMap = (Element) map.item(i);

                String name = currentMap.getElementsByTagName("name").item(0).getTextContent();

                NodeList spawnLocationNodeList = ((Element) map.item(0)).getElementsByTagName("spawn-location").item(0)
                        .getChildNodes();
                float spawnX = Float.parseFloat(spawnLocationNodeList.item(1).getTextContent());
                float spawnY = Float.parseFloat(spawnLocationNodeList.item(3).getTextContent());
                float spawnZ = Float.parseFloat(spawnLocationNodeList.item(5).getTextContent());
                float rotation = Float.parseFloat(spawnLocationNodeList.item(7).getTextContent());

                Vector spawnLocation = new Vector(spawnX, spawnY, spawnZ);
                float spawnRotation = rotation;

                Element bounds = (Element) currentMap.getElementsByTagName("bounds").item(0);

                NodeList min = bounds.getElementsByTagName("min").item(0).getChildNodes();
                float minX = Float.parseFloat(min.item(1).getTextContent());
                float minY = Float.parseFloat(min.item(3).getTextContent());
                float minZ = Float.parseFloat(min.item(5).getTextContent());

                Vector minBound = new Vector(minX, minY, minZ);

                NodeList max = bounds.getElementsByTagName("max").item(0).getChildNodes();
                float maxX = Float.parseFloat(max.item(1).getTextContent());
                float maxY = Float.parseFloat(max.item(3).getTextContent());
                float maxZ = Float.parseFloat(max.item(5).getTextContent());

                Vector maxBound = new Vector(maxX, maxY, maxZ);

                maps.add(new Map(name, spawnLocation, spawnRotation, minBound, maxBound));
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
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
