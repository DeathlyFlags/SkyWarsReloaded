package com.walrusone.skywars.powerups;

import com.walrusone.skywars.game.Game;
import com.walrusone.skywars.game.Game.GameState;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

class PowerUpTask extends BukkitRunnable {
    private Game game;

    public PowerUpTask(Game game) {
        if (game == null)
            throw new IllegalArgumentException("The Game can't be null.");
        this.game = game;
    }

    @Override
    public void run() {
        if (game.getGameState() != GameState.PLAYING) this.cancel();
        for (Location loc : game.getGameMap().getSpawns().values()) {
            int rand = new Random().nextInt(6);
            Location dropZone = loc.getWorld().getHighestBlockAt(loc).getLocation().add(0, 0.6, 0);
            switch (rand) {
                case '0':
                    FireResistance.dropPowerUp(dropZone);
                    break;
                case '1':
                    Jump.dropPowerUp(dropZone);
                    break;
                case '2':
                    Poison.dropPowerUp(dropZone);
                    break;
                case '3':
                    Slowness.dropPowerUp(dropZone);
                    break;
                case '4':
                    Speed.dropPowerUp(dropZone);
                    break;
                case '5':
                    Strength.dropPowerUp(dropZone);
                    break;
            }
        }
    }
}
