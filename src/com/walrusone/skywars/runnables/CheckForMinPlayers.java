package com.walrusone.skywars.runnables;

import com.walrusone.skywars.game.Game;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckForMinPlayers extends BukkitRunnable {

    private final Game game;

    public CheckForMinPlayers(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        game.prepareForStart();
    }
}
