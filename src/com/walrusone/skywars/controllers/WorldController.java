package com.walrusone.skywars.controllers;

import com.walrusone.skywars.SkyWarsReloaded;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorldController {

    public World createEmptyWorld(String name) {
        WorldCreator worldCreator = new WorldCreator(name);
        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.generateStructures(false);
        worldCreator.generator(getChunkGenerator());

        World world = worldCreator.createWorld();
        world.setDifficulty(Difficulty.NORMAL);
        world.setSpawnFlags(true, true);
        world.setPVP(true);
        world.setStorm(false);
        world.setThundering(false);
        world.setWeatherDuration(Integer.MAX_VALUE);
        world.setAutoSave(false);
        world.setKeepSpawnInMemory(false);
        world.setTicksPerAnimalSpawns(1);
        world.setTicksPerMonsterSpawns(1);

        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("mobGriefing", "false");
        world.setGameRuleValue("doFireTick", "false");
        world.setGameRuleValue("showDeathMessages", "false");

        Block b = world.getBlockAt(0, 20, 0);
        b.setType(Material.STONE);

        return world;
    }

    public boolean loadWorld(String worldName) {
        String isLobby = worldName.substring(0, Math.min(worldName.length(), 5));
        WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.generateStructures(false);
        worldCreator.generator(getChunkGenerator());
        World world = worldCreator.createWorld();
        world.setDifficulty(Difficulty.NORMAL);
        world.setSpawnFlags(true, true);
        if (isLobby.equalsIgnoreCase("lobby")) {
            world.setPVP(false);
        } else {
            world.setPVP(true);
        }
        world.setStorm(false);
        world.setThundering(false);
        world.setWeatherDuration(Integer.MAX_VALUE);
        world.setAutoSave(false);
        world.setKeepSpawnInMemory(false);
        world.setTicksPerAnimalSpawns(1);
        world.setTicksPerMonsterSpawns(1);

        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("mobGriefing", "false");
        world.setGameRuleValue("doFireTick", "false");
        world.setGameRuleValue("showDeathMessages", "false");

        boolean loaded = false;
        for (World w : SkyWarsReloaded.get().getServer().getWorlds()) {
            if (w.getName().equals(world.getName())) {
                loaded = true;
                break;
            }
        }
        return loaded;
    }

    public void unloadWorld(String w) {
        World world = SkyWarsReloaded.get().getServer().getWorld(w);
        if (world != null) {
            SkyWarsReloaded.get().getServer().unloadWorld(world, true);
        }
    }

    public void deleteWorld(String name) {
        unloadWorld(name);
        File target = new File(SkyWarsReloaded.get().getServer().getWorldContainer().getAbsolutePath(), name);
        deleteWorld(target);
    }

    public void deleteWorld(File path) {
        if (path.exists()) {
            File files[] = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.isDirectory()) file.delete();
                    else deleteWorld(file);
                }
            }
        }
        path.delete();
    }


    private ChunkGenerator getChunkGenerator() {
        return new ChunkGenerator() {
            @Override
            public List<BlockPopulator> getDefaultPopulators(World world) {
                return Arrays.asList(new BlockPopulator[0]);
            }

            @Override
            public boolean canSpawn(World world, int x, int z) {
                return true;
            }

            @SuppressWarnings("deprecation")
            @Override
            public byte[] generate(World world, Random random, int x, int z) {
                return new byte[32768];
            }

            @Override
            public Location getFixedSpawnLocation(World world, Random random) {
                return new Location(world, 0.0D, 64.0D, 0.0D);
            }
        };
    }
}
