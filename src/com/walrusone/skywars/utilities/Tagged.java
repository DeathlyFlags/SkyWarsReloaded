package com.walrusone.skywars.utilities;

import com.walrusone.skywars.game.GamePlayer;

public class Tagged {
    private final GamePlayer player;
    private final Long time;

    public Tagged(GamePlayer player, Long time) {
        this.player = player;
        this.time = time;
    }

    public GamePlayer getPlayer() {
        return player;
    }

    public Long getTime() {
        return time;
    }
}
