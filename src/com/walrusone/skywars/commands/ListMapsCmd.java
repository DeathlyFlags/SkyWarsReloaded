package com.walrusone.skywars.commands;

import com.walrusone.skywars.SkyWarsReloaded;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class ListMapsCmd extends BaseCmd {

    public ListMapsCmd() {
        forcePlayer = false;
        cmdName = "list";
        argLength = 1; //counting cmdName
        usage = "";
        desc = "::  List all maps currently available";

    }

    @Override
    public boolean run() {
        sender.sendMessage(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Maps (" + ChatColor.GREEN + "Green = registered" + ChatColor.BLUE + ", " + ChatColor.RED + "Red = unregistered" + ChatColor.BLUE + ")");
        ArrayList<String> maps = SkyWarsReloaded.getMC().getEditMaps();
        for (String name : maps) {
            if (SkyWarsReloaded.getMC().mapRegistered(name)) {
                sender.sendMessage(ChatColor.GREEN + name);
            } else {
                sender.sendMessage(ChatColor.RED + name);
            }
        }
        return true;
    }

}
