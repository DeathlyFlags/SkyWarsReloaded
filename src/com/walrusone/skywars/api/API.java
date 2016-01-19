package com.walrusone.skywars.api;

import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.game.GamePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

class API {

    public static int getPoints(Player player) {
        GamePlayer gplayer = SkyWarsReloaded.getPC().getPlayer(player.getUniqueId());
        return gplayer.getBalance();
    }

    public static int getPoints(UUID id) {
        GamePlayer gplayer = SkyWarsReloaded.getPC().getPlayer(id);
        return gplayer.getBalance();
    }
}
