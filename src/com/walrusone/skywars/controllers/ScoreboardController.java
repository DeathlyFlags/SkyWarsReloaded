package com.walrusone.skywars.controllers;

import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.game.GamePlayer;
import com.walrusone.skywars.utilities.Messaging;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.text.DecimalFormat;

public class ScoreboardController {

    public void getScoreboard(Player player) {
        if (player != null && SkyWarsReloaded.getCfg().LobbyScoreboardEnabeld()) {
            try {
                GamePlayer gPlayer = SkyWarsReloaded.getPC().getPlayer(player.getUniqueId());
                DecimalFormat df = new DecimalFormat("#.##");

                double kd;
                if (gPlayer.getDeaths() <= 0) {
                    kd = 0.00;
                } else {
                    kd = (double) gPlayer.getKills() / gPlayer.getDeaths();
                }

                double money;
                if (SkyWarsReloaded.getCfg().usingExternalEcomony()) {
                    money = SkyWarsReloaded.econ.getBalance(player);
                } else {
                    money = (double) gPlayer.getBalance();
                }

                String moneyValue = new Messaging.MessageFormatter().setVariable("value", df.format(money)).format("scoreboard.money");
                String kdValue = new Messaging.MessageFormatter().setVariable("value", df.format(kd)).format("scoreboard.kd");
                String score = new Messaging.MessageFormatter().setVariable("value", "" + gPlayer.getScore()).format("scoreboard.score");
                String wins = new Messaging.MessageFormatter().setVariable("value", "" + gPlayer.getWins()).format("scoreboard.wins");
                String kills = new Messaging.MessageFormatter().setVariable("value", "" + gPlayer.getKills()).format("scoreboard.kills");
                String gamesPlayed = new Messaging.MessageFormatter().setVariable("value", "" + gPlayer.getGamesPlayed()).format("scoreboard.games-played");
                String title = new Messaging.MessageFormatter().format("scoreboard.title");

                ScoreboardManager manager = SkyWarsReloaded.get().getServer().getScoreboardManager();

                Scoreboard board = manager.getNewScoreboard();
                Objective stats = board.registerNewObjective("stats", "dummy");
                stats.setDisplaySlot(DisplaySlot.SIDEBAR);
                stats.setDisplayName(title);

                Score moneyScore = stats.getScore(moneyValue);
                moneyScore.setScore(6);
                Score scoreScore = stats.getScore(score);
                scoreScore.setScore(5);
                Score winsScore = stats.getScore(wins);
                winsScore.setScore(4);
                Score killsScore = stats.getScore(kills);
                killsScore.setScore(3);
                Score gamesScore = stats.getScore(gamesPlayed);
                gamesScore.setScore(2);
                Score kdScore = stats.getScore(kdValue);
                kdScore.setScore(1);
                if (player != null) {
                    player.setScoreboard(board);
                }
            } catch (NullPointerException ignored) {
            }

        }
    }
}
