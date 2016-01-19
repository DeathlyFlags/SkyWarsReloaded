package com.walrusone.skywars.controllers;

import com.google.common.collect.Maps;
import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.game.Game;
import com.walrusone.skywars.game.Game.GameState;
import com.walrusone.skywars.game.GamePlayer;
import com.walrusone.skywars.utilities.Messaging;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GameController {

    private final Map<Integer, Game> gameNumbers = Maps.newHashMap();
    private final Map<Integer, GameSign> signJoinGames = Maps.newHashMap();
    private final Queue<GamePlayer> waitingPlayers = new LinkedList<>();
    private boolean shutdown = false;
    private int gameNumber = 0;

    public Game findGame() {
        Optional<Game> opt = getGames()
                .stream()
                .filter(game -> !game.isFull()
                        && game.getState() == GameState.PREGAME).findAny();
        if (opt.isPresent())
            return opt.get();
        else
            return createGame();
    }

    public Game createGame() {
        gameNumber = findAvailableNumber();
        if (gameNumber == -1)
            return null;

        Game game = new Game(gameNumber, "");
        gameNumbers.put(gameNumber, game);
        if (waitingPlayers.isEmpty())
            return game;
        while (!game.isFull() && !waitingPlayers.isEmpty())
            game.addPlayer(waitingPlayers.remove());
        if (game.isFull())
            createGame();
        return game;
    }

    private int findAvailableNumber() {
        int maxNum = SkyWarsReloaded.getCfg().getMaxNumberOfGames();
        if (maxNum == -1)
            maxNum = Integer.MAX_VALUE;
        int gameNumber = -1;

        for (int i = 1; i <= maxNum; i++)
            if (gameNumbers.get(i) == null)
                return i;
        return gameNumber;
    }

    public void signJoinLoad() {
        File signJoinFile = new File(SkyWarsReloaded.get().getDataFolder(),
                "signJoinGames.yml");

        if (!signJoinFile.exists())
            SkyWarsReloaded.get().saveResource("signJoinGames.yml", false);

        if (signJoinFile.exists()) {
            FileConfiguration storage = YamlConfiguration
                    .loadConfiguration(signJoinFile);
            try {
                for (String gameNumber : storage.getConfigurationSection(
                        "games.").getKeys(false)) {
                    String mapName = storage.getString("games." + gameNumber
                            + ".map");
                    String world = storage.getString("games." + gameNumber
                            + ".world");
                    if (mapName == null || world == null)
                        return;
                    GameSign gs = new GameSign(storage.getInt("games."
                            + gameNumber + ".x"), storage.getInt("games."
                            + gameNumber + ".y"), storage.getInt("games."
                            + gameNumber + ".z"), world, mapName);
                    signJoinGames.put(Integer.valueOf(gameNumber), gs);
                    createGame(Integer.valueOf(gameNumber), gs);
                }
            } catch (NullPointerException ignored) {
            }
        }
    }

    public boolean addSignJoinGame(Location loc, String mapName) {
        if (!SkyWarsReloaded.getMC().mapRegistered(mapName))
            return false;
        String world = loc.getWorld().getName();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        GameSign gs = new GameSign(x, y, z, world, mapName);
        gameNumber = -1;
        File signJoinFile = new File(SkyWarsReloaded.get().getDataFolder(),
                "signJoinGames.yml");
        if (!signJoinFile.exists())
            SkyWarsReloaded.get().saveResource("signJoinGames.yml", false);

        if (!signJoinFile.exists())
            return false;
        FileConfiguration storage = YamlConfiguration
                .loadConfiguration(signJoinFile);
        for (int i = 1; i < 1000; i++) {
            if (storage.getString("games." + i + ".map") == null) {
                gameNumber = i;
                break;
            }
        }
        storage.set("games." + gameNumber + ".x", x);
        storage.set("games." + gameNumber + ".y", y);
        storage.set("games." + gameNumber + ".z", z);
        storage.set("games." + gameNumber + ".world", world);
        storage.set("games." + gameNumber + ".map", mapName);
        try {
            storage.save(signJoinFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        signJoinGames.put(gameNumber, gs);
        createGame(gameNumber, gs);
        return true;

    }

    public void removeSignJoinGame(String gameNumber) {
        File signJoinFile = new File(SkyWarsReloaded.get().getDataFolder(),
                "signJoinGames.yml");
        FileConfiguration storage = YamlConfiguration
                .loadConfiguration(signJoinFile);
        storage.set("games." + gameNumber, null);
        try {
            storage.save(signJoinFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        signJoinGames.remove(Integer.valueOf(gameNumber));
        Game game = getGame(Integer.valueOf(gameNumber));
        if (game == null)
            return;
        if (game.getState() == GameState.PLAYING)
            return;
        game.endGame();
    }

    private void createGame(int gameNumber, GameSign gs) {
        Game game = new Game(gameNumber, gs.getName());
        gameNumbers.put(gameNumber, game);
        updateSign(gameNumber);
    }

    public void updateSign(int gameNumber) {
        GameSign gameSign = signJoinGames.get(gameNumber);
        if (gameSign == null)
            return;
        World world = SkyWarsReloaded.get().getServer()
                .getWorld(gameSign.getWorld());
        if (world == null)
            return;
        Block b = world.getBlockAt(gameSign.getX(), gameSign.getY(),
                gameSign.getZ());
        if (b.getType() != Material.WALL_SIGN
                && b.getType() != Material.SIGN_POST)
            return;
        Sign s = (Sign) b.getState();

        org.bukkit.material.Sign meteSign = (org.bukkit.material.Sign) b.getState().getData();
        Block attachedBlock = b.getRelative(meteSign.getAttachedFace());
        String state = getStatusName(getGame(gameNumber));
        setMaterial(getStatus(getGame(gameNumber)), attachedBlock);

        if (s == null)
            return;

        int max = getGame(gameNumber).getNumberOfSpawns();
        int count = getGame(gameNumber).getPlayers().size();

        s.getBlock().getChunk().load();
        s.setLine(0,
                new Messaging.MessageFormatter().format("signJoinSigns.line1"));
        s.setLine(
                1,
                new Messaging.MessageFormatter().setVariable("mapName",
                        gameSign.getName().toUpperCase()).format(
                        "signJoinSigns.line2"));
        s.setLine(
                2,
                new Messaging.MessageFormatter().setVariable("gameStatus",
                        state).format("signJoinSigns.line3"));
        s.setLine(
                3,
                new Messaging.MessageFormatter()
                        .setVariable("count", "" + count)
                        .setVariable("max", "" + max)
                        .format("signJoinSigns.line4"));
        s.update();
    }

    private GameStatus getStatus(Game game) {
        if (game.getState() == GameState.PREGAME && !game.isFull())
            return GameStatus.JOINABLE;
        else if (game.getState() == GameState.PREGAME && game.isFull())
            return GameStatus.FULL;
        else if (game.getState() == GameState.PLAYING)
            return GameStatus.INPROGRESS;
        else
            return GameStatus.RESTARTING;
    }

    private String getStatusName(Game game) {
        if (game.getState() == GameState.PREGAME && !game.isFull())
            return new Messaging.MessageFormatter()
                    .format("signJoinSigns.joinable");
        else if (game.getState() == GameState.PREGAME && game.isFull())
            return new Messaging.MessageFormatter()
                    .format("signJoinSigns.full");
        else if (game.getState() == GameState.PLAYING)
            return new Messaging.MessageFormatter()
                    .format("signJoinSigns.inprogress");
        else
            return new Messaging.MessageFormatter()
                    .format("signJoinSigns.restarting");
    }

    @SuppressWarnings("deprecation")
    private void setMaterial(GameStatus gs, Block attachedBlock) {
        String material = SkyWarsReloaded.getCfg().getSignJoinMaterial();
        Material sMat = null;
        if (material.equalsIgnoreCase("wool"))
            sMat = Material.WOOL;
        else if (material.equalsIgnoreCase("clay"))
            sMat = Material.STAINED_CLAY;
        else if (material.equalsIgnoreCase("glass"))
            sMat = Material.STAINED_GLASS;

        if (sMat == null)
            return;

        if (gs == GameStatus.JOINABLE) {
            attachedBlock.setType(sMat);
            attachedBlock.setData((byte) 5);
        } else if (gs == GameStatus.FULL || gs == GameStatus.INPROGRESS) {
            attachedBlock.setType(sMat);
            attachedBlock.setData((byte) 14);
        } else if (gs == GameStatus.RESTARTING) {
            attachedBlock.setType(sMat);
            attachedBlock.setData((byte) 11);
        }
    }

    public void deleteGame(final int gameNumber) {
        if (shutdown) return;

        final GameSign gs = signJoinGames.get(gameNumber);
        gameNumbers.remove(gameNumber);

        if (SkyWarsReloaded.getCfg().bungeeEnabled()) {
            SkyWarsReloaded.get().getServer().getScheduler()
                    .runTaskLater(SkyWarsReloaded.get(), this::createGame, 40);
        }
        if (SkyWarsReloaded.getCfg().signJoinMode() && gs != null) {
            SkyWarsReloaded.get().getServer().getScheduler()
                    .runTaskLater(SkyWarsReloaded.get(), () -> createGame(gameNumber, gs), 40);
        }
    }

    public void shutdown() {
        shutdown = true;
        for (Game game : getGames()) {
            game.shutdown();
            game.endGame();
        }
    }

    public List<Game> getGames() {
        return new ArrayList<>(gameNumbers.values());
    }

    public Game getGame(int gameNumber) {
        return gameNumbers.get(gameNumber);
    }

    public void addToQueue(GamePlayer gPlayer) {
        if (!waitingPlayers.contains(gPlayer))
            waitingPlayers.add(gPlayer);
    }

    public enum GameStatus {
        JOINABLE, FULL, INPROGRESS, RESTARTING,
    }

    private class GameSign {
        private final int x;
        private final int y;
        private final int z;
        private final String mapName;
        private final String world;

        GameSign(int x, int y, int z, String world, String mapName) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.world = world;
            this.mapName = mapName;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public String getWorld() {
            return world;
        }

        public String getName() {
            return mapName;
        }
    }
}
