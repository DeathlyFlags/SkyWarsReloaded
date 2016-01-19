package com.walrusone.skywars.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player winner;
    private final String mapName;

    public GameEndEvent(Player winner, String mapName) {
        this.winner = winner;
        this.mapName = mapName;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getWinner() {
        return winner;
    }

    public String getMap() {
        return mapName;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
