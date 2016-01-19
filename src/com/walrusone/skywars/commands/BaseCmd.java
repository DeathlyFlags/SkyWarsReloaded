package com.walrusone.skywars.commands;

import com.walrusone.skywars.utilities.Messaging;
import com.walrusone.skywars.utilities.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

abstract class BaseCmd {

    public String cmdName;
    CommandSender sender;
    String[] args;
    int argLength = 0;
    boolean forcePlayer = true;
    String usage = "";
    Player player;
    String desc = "";

    BaseCmd() {

    }

    public void processCmd(CommandSender s, String[] arg) {
        sender = s;
        args = arg;

        if (forcePlayer) {
            if (!(s instanceof Player)) {
                sender.sendMessage(new Messaging.MessageFormatter().format("error.must-be-player"));
                return;
            } else {
                player = (Player) s;
            }
        }

        if (!Util.hp(sender, cmdName))
            sender.sendMessage(new Messaging.MessageFormatter().format("error.cmd-no-perm"));
        else if (argLength > arg.length)
            s.sendMessage(ChatColor.DARK_RED + "Wrong usage: " + ChatColor.GRAY + "/swr " + helper());
    }

    protected abstract boolean run();


    public String helper() {
        return ChatColor.RED + cmdName + " " + usage + " " + ChatColor.GRAY + desc;
    }
}
