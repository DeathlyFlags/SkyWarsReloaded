package com.walrusone.skywars.commands;

import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.game.Game;
import com.walrusone.skywars.game.GamePlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
                             String commandLabel, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("swr.quit")) {
            GamePlayer gPlayer = SkyWarsReloaded.getPC().getPlayer(
                    player.getUniqueId());
            if (gPlayer.inGame() && !gPlayer.isSpectating()) {
                Game game = gPlayer.getGame();
                game.deletePlayer(gPlayer, true, false);
                return true;
            } else if (SkyWarsReloaded.getCfg().spectatingEnabled()) {
                if (gPlayer.isSpectating()) {
                    Game game = gPlayer.getGame();
                    gPlayer.spectateMode(false, game, game.getSpawn(), false);
                }
                return true;
            }
            return true;
        } else {
            player.sendMessage(ChatColor.RED
                    + "You do not have permission to use swr.quit!");
            return true;
        }
    }
}
