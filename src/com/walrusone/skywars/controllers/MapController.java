package com.walrusone.skywars.controllers;

import com.google.common.collect.Maps;
import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.game.GameMap;
import com.walrusone.skywars.utilities.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class MapController {

    private final Map<String, GameMap> mapList = Maps.newHashMap();
    private final File maps;
    private final File rootDirectory;
    private final ArrayList<String> editMaps = new ArrayList<>();

    public MapController() {
        File dataDirectory = SkyWarsReloaded.get().getDataFolder();
        maps = new File(dataDirectory, "maps");
        String root = SkyWarsReloaded.get().getServer().getWorldContainer().getAbsolutePath();
        rootDirectory = new File(root);
        if (maps.exists() && maps.isDirectory()) {
            for (File map : maps.listFiles()) {
                if (map.isDirectory()) {
                    registerMap(map.getName());
                    editMaps.add(map.getName().toLowerCase());
                }
            }
        } else {
            SkyWarsReloaded.get().getLogger().info("Maps directory is missing or no Maps were found!");
        }
    }

    public boolean registerMap(String name) {
        boolean result;
        File source = new File(maps, name);
        File target = new File(rootDirectory, name);
        Util.copyFiles(source, target);
        boolean mapExists = false;
        if (target.isDirectory()) {
            if (target.list().length > 0) {
                mapExists = true;
            }
        }
        if (mapExists) {
            SkyWarsReloaded.getWC().deleteWorld(name);
        }

        Util.copyFiles(source, target);

        boolean loaded = SkyWarsReloaded.getWC().loadWorld(name);
        if (loaded) {
            GameMap gameMap = new GameMap(name, source);
            if (gameMap.containsSpawns()) {
                mapList.put(name.toLowerCase(), gameMap);
                SkyWarsReloaded.get().getLogger().info("Registered Map " + name + "!");
                result = true;
            } else {
                if (name.equalsIgnoreCase("lobby")) {
                    SkyWarsReloaded.get().getLogger().info("Could Not Register Map: " + name + " - Map must have at least 1 Spawn Point!");
                    result = false;
                } else {
                    SkyWarsReloaded.get().getLogger().info("Could Not Register Map: " + name + " - Map must have at least 2 Spawn Points");
                    result = false;
                }
            }
        } else {
            SkyWarsReloaded.get().getLogger().info("Could Not Load Map: " + name);
            result = false;
        }
        SkyWarsReloaded.getWC().deleteWorld(name);
        return result;
    }

    public GameMap getMap(String name) {
        return mapList.get(name);
    }

    public ArrayList<GameMap> getRegisteredMaps() {
        return new ArrayList<>(mapList.values());
    }

    public void removeMap(String name) {
        mapList.remove(name.toLowerCase());
    }

    public ArrayList<String> getMaps() {
        return new ArrayList<>(mapList.keySet());
    }

    public void addEditMap(String name) {
        editMaps.add(name.toLowerCase());
    }

    public void removeEditMap(String name) {
        editMaps.remove(name.toLowerCase());
    }

    public ArrayList<String> getEditMaps() {
        return editMaps;
    }

    public boolean mapExists(String name) {
        return editMaps.contains(name.toLowerCase());
    }

    public boolean mapRegistered(String name) {
        return mapList.containsKey(name.toLowerCase());
    }

}
