package com.walrusone.skywars.game;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.utilities.EmptyChest;
import com.walrusone.skywars.utilities.Util;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.inventory.InventoryHolder;

import java.io.File;
import java.util.Map;
import java.util.Set;

public class GameMap {

    private final File source;
    private final File rootDirectory;
    private final String name;
    private final Map<Integer, Location> spawnPoints = Maps.newHashMap();
    private final Set<EmptyChest> chests = Sets.newHashSet();
    private final int minX;
    private final int minZ;
    private final int maxX;
    private final int maxZ;

    public GameMap(String name, File filepath) {
        int size = SkyWarsReloaded.getCfg().getMaxMapSize();
        int max = size / 2;
        int min = -size / 2;
        minX = min;
        minZ = min;
        maxX = max;
        maxZ = max;
        this.source = filepath;
        this.name = name.toLowerCase();
        String root = SkyWarsReloaded.get().getServer().getWorldContainer()
                .getAbsolutePath();
        rootDirectory = new File(root);
        ChunkIterator();
    }

    public String getName() {
        return name;
    }

    public boolean containsSpawns() {
        if (!name.equalsIgnoreCase("lobby") && spawnPoints.size() >= 2) {
            return true;
        } else if (name.equalsIgnoreCase("lobby") && spawnPoints.size() >= 1) {
            return true;
        }
        return false;
    }

    public Map<Integer, Location> getSpawns() {
        return spawnPoints;
    }

    public Set<EmptyChest> getChests() {
        return chests;
    }

    public boolean loadMap(int gNumber) {
        String mapName = name + "_" + gNumber;
        boolean mapExists = false;
        File target = new File(rootDirectory, mapName);
        if (target.isDirectory()) {
            if (target.list().length > 0) {
                mapExists = true;
            }
        }
        if (mapExists) {
            SkyWarsReloaded.getWC().deleteWorld(mapName);
        }

        Util.copyFiles(source, target);

        boolean loaded = SkyWarsReloaded.getWC().loadWorld(mapName);
        if (loaded) {
            World world = SkyWarsReloaded.get().getServer().getWorld(mapName);
            world.setAutoSave(false);
            world.setThundering(false);
            world.setStorm(false);
            world.setDifficulty(Difficulty.NORMAL);
            world.setSpawnLocation(2000, 0, 2000);
            world.setTicksPerAnimalSpawns(1);
            world.setTicksPerMonsterSpawns(1);
            world.setGameRuleValue("doMobSpawning", "false");
            world.setGameRuleValue("mobGriefing", "false");
            world.setGameRuleValue("doFireTick", "false");
            world.setGameRuleValue("showDeathMessages", "false");
        }
        return loaded;
    }

    private void ChunkIterator() {
        World chunkWorld = SkyWarsReloaded.get().getServer().getWorld(name);
        int minY = 0;
        Block min = chunkWorld.getBlockAt(minX, minY, minZ);
        int maxY = 0;
        Block max = chunkWorld.getBlockAt(maxX, maxY, maxZ);
        Chunk cMin = min.getChunk();
        Chunk cMax = max.getChunk();
        int countSpawns = 1;

        for (int cx = cMin.getX(); cx < cMax.getX(); cx++) {
            for (int cz = cMin.getZ(); cz < cMax.getZ(); cz++) {
                Chunk currentChunk = chunkWorld.getChunkAt(cx, cz);
                currentChunk.load(true);

                for (BlockState te : currentChunk.getTileEntities()) {
                    if (te instanceof Beacon) {
                        Beacon beacon = (Beacon) te;
                        Location loc = beacon.getLocation();
                        spawnPoints.put(countSpawns, loc);
                        countSpawns++;
                    } else if (te instanceof Chest) {
                        Chest chest = (Chest) te;
                        InventoryHolder ih = chest.getInventory().getHolder();
                        chests.add(new EmptyChest(chest.getX(), chest.getY(),
                                chest.getZ(), ih instanceof DoubleChest));
                    }
                }
            }
        }
    }

}
