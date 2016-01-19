package com.walrusone.skywars.game;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.controllers.WorldController;
import com.walrusone.skywars.utilities.EmptyChest;
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

	private File source;
	private File rootDirectory;
	private String name;
	private Map<Integer, Location> spawnPoints = Maps.newHashMap();
	private Set<EmptyChest> chests = Sets.newHashSet();
	private Block min;
	private Block max;
	private int minX;
	private int minZ;
	private int minY = 0;
	private int maxX;
	private int maxZ;
	private int maxY = 0;

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
		WorldController wc = SkyWarsReloaded.getWC();
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

		wc.copyWorld(source, target);

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

	public void ChunkIterator() {
		World chunkWorld = SkyWarsReloaded.get().getServer().getWorld(name);
		min = chunkWorld.getBlockAt(minX, minY, minZ);
		max = chunkWorld.getBlockAt(maxX, maxY, maxZ);
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
