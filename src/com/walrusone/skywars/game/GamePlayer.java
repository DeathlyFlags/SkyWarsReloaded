package com.walrusone.skywars.game;

import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.utilities.Tagged;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GamePlayer {

    private final UUID uuid;
    private final List<String> permissions = new ArrayList<>();
    private boolean isSpectating = false;
    private String name;
    private String color;
    private String effect;
    private String projEffect;
    private int wins;
    private int kills;
    private int deaths;
    private int gamesPlayed;
    private int score;
    private int blocksPlaced;
    private int balance;
    private boolean hasKitSelected;
    private GameKit selectedKit;
    private Tagged taggedBy;
    private boolean inGame = false;
    private int game = -1;
    private int specGame = -1;
    private int votedForOP = 0;
    private int timeVote = 0;
    private int jumpVote = 0;
    private int weatherVote = 0;
    private List<String> newPerms = new ArrayList<>();

    public GamePlayer(UUID uuid) {
        this.uuid = uuid;
        SkyWarsReloaded.getDS().loadPlayer(this);
        name = getP().getName();
        setTagged(this);
    }

    public Player getP() {
        return SkyWarsReloaded.get().getServer().getPlayer(uuid);
    }

    public UUID getUUID() {
        return uuid;
    }

    public GamePlayer getGamePlayer() {
        return this;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int x) {
        balance = x;
    }

    public String getName() {
        if (getP() != null) {
            name = getP().getName();
            return name;
        } else {
            return name;
        }
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int w) {
        wins = w;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int k) {
        kills = k;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int d) {
        deaths = d;
    }

    public boolean isSpectating() {
        return isSpectating;
    }

    private void setSpectating(boolean b) {
        isSpectating = b;
    }

    public Tagged getTagged() {
        return taggedBy;
    }

    public void setTagged(GamePlayer player) {
        taggedBy = new Tagged(player, System.currentTimeMillis());
    }

    public Game getGame() {
        return SkyWarsReloaded.getGC().getGame(game);
    }

    public void setGame(int game) {
        this.game = game;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gp) {
        gamesPlayed = gp;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int s) {
        score = s;
    }

    public void setKitSelected(boolean b) {
        hasKitSelected = b;
    }

    public boolean hasKitSelected() {
        return hasKitSelected;
    }

    public GameKit getSelectedKit() {
        return selectedKit;
    }

    public void setSelectedKit(GameKit kit) {
        selectedKit = kit;
    }

    public int getBlocks() {
        return blocksPlaced;
    }

    public void setBlocks(int s) {
        blocksPlaced = s;
    }

    public Game getSpecGame() {
        return SkyWarsReloaded.getGC().getGame(specGame);
    }

    private void setSpecGame(int game) {
        specGame = game;
    }

    public void setInGame(boolean state) {
        inGame = state;
    }

    public boolean inGame() {
        return inGame;
    }

    public void spectateMode(final boolean state, Game game, Location location,
                             boolean shutdown) {
        if (getP() != null) {
            setSpectating(state);
            if (state) {
                game.addSpectator(this);
                game.deletePlayer(this, true, false);
                for (Player target : SkyWarsReloaded.get().getServer()
                        .getOnlinePlayers()) {
                    target.hidePlayer(getP());
                }
                getP().setFoodLevel(20);
                getP().setScoreboard(game.getScoreboard());
                getP().setGameMode(GameMode.SPECTATOR);
            } else {
                setSpecGame(-1);
                for (Player target : SkyWarsReloaded.get().getServer()
                        .getOnlinePlayers()) {
                    target.showPlayer(getP());
                }
                getP().setScoreboard(
                        SkyWarsReloaded.get().getServer()
                                .getScoreboardManager().getNewScoreboard());
                for (PotionEffect effect : getP().getActivePotionEffects()) {
                    getP().removePotionEffect(effect.getType());
                }
                getP().setFireTicks(0);
                getP().setGameMode(GameMode.SURVIVAL);
            }
            getP().teleport(location, TeleportCause.PLUGIN);
            if ((!state && !shutdown) || state) {
                SkyWarsReloaded
                        .get()
                        .getServer()
                        .getScheduler()
                        .scheduleSyncDelayedTask(SkyWarsReloaded.get(),
                                () -> {
                                    if (getP() != null) {
                                        getP().setAllowFlight(state);
                                        getP().setFlying(state);
                                        SkyWarsReloaded.getInvC()
                                                .restoreInventory(getP());
                                    }
                                }, 5);
            }
        }
    }

	/*
     * private void giveSpectateItems() { if (getP() != null) {
	 * getP().getInventory().clear();
	 * getP().getInventory().setItem(SkyWarsReloaded.getCfg().getExitItemSlot(),
	 * SkyWarsReloaded.getCfg().getExitGameItem()); if
	 * (SkyWarsReloaded.getCfg().spectateShopEnabled()) { if
	 * (getP().hasPermission("swr.spectateshop")) {
	 * getP().getInventory().setItem
	 * (SkyWarsReloaded.getCfg().getSpecStoreItemSlot(),
	 * SkyWarsReloaded.getCfg().getSpecStoreMenuItem()); } }
	 * getP().getInventory(
	 * ).setItem(SkyWarsReloaded.getCfg().getSpectateItemSlot(),
	 * SkyWarsReloaded.getCfg().getSpectatePlayerItem()); } }
	 */

    public boolean gamemodeChangeAllowed() {
        return !isSpectating;
    }

    public int getOpVote() {
        return votedForOP;
    }

    public void setOpVote(int vote) {
        votedForOP = vote;
    }

    public int getJumpVote() {
        return jumpVote;
    }

    public void setJumpVote(int vote) {
        jumpVote = vote;
    }

    public int getWeatherVote() {
        return weatherVote;
    }

    public void setWeatherVote(int vote) {
        weatherVote = vote;
    }

    public int getTimeVote() {
        return timeVote;
    }

    public void setTimeVote(int i) {
        timeVote = i;
    }

    public String getGlass() {
        if (color == null)
            color = "normal";
        return color;
    }

    public void setGlass(String name2) {
        if (name2 != null)
            color = name2;
        else
            color = "normal";
    }

    public List<String> getPerms() {
        return permissions;
    }

    public void setPerms(List<String> perms) {
        permissions.addAll(perms.stream().collect(Collectors.toList()));
    }

    public void addPerm(String perm) {
        newPerms.add(perm);
        permissions.add(perm);
    }

    public void clearNewPerms() {
        newPerms = null;
        newPerms = new ArrayList<>();
    }

    public List<String> getNewPerms() {
        return newPerms;
    }

    public boolean hasPerm(String perm) {
        return permissions.contains(perm);
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        if (effect != null) {
            this.effect = effect;
        } else {
            this.effect = "normal";
        }

    }

    public String getProjEffect() {
        return projEffect;
    }

    public void setProjEffect(String effect) {
        if (effect != null) {
            this.projEffect = effect;
        } else {
            this.projEffect = "normal";
        }

    }

    public void clearPlayer() {
        Player p = this.getP();
        p.getInventory().clear();
        p.getInventory().setArmorContents(new ItemStack[4]);
        p.setLevel(0);
        p.setExp(0);
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setFlying(false);
        p.setGameMode(GameMode.ADVENTURE);
        this.setOpVote(0);
        this.setWeatherVote(0);
        this.setTimeVote(0);
        this.setJumpVote(0);
        for (PotionEffect effect : p.getActivePotionEffects())
            p.removePotionEffect(effect.getType());
    }

}
