package com.walrusone.skywars.menus;

import com.google.common.collect.Lists;
import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.game.Game.GameState;
import com.walrusone.skywars.game.GamePlayer;
import com.walrusone.skywars.utilities.GlassColor;
import com.walrusone.skywars.utilities.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class UseColorMenu {

    private static final int menuSlotsPerRow = 9;
    private static final int menuSize = 36;
    private static final String menuName = new Messaging.MessageFormatter().format("menu.usecolor-menu-title");
    private static final String premissionPrefix = "swr.colorglass.";

    public UseColorMenu(final GamePlayer gamePlayer) {
        List<GlassColor> availableItems = SkyWarsReloaded.getGLC().getColorItems();

        int rowCount = menuSlotsPerRow;
        while (rowCount < 36 && rowCount < menuSize) {
            rowCount += menuSlotsPerRow;
        }

        SkyWarsReloaded.getIC().create(gamePlayer.getP(), menuName, rowCount, event -> {
            if (!gamePlayer.inGame()) {
                gamePlayer.getP().closeInventory();
                return;
            }

            if (gamePlayer.getGame().getState() != GameState.PREGAME) {
                gamePlayer.getP().closeInventory();
                return;
            }

            String name = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', event.getName()));
            if (name.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', new Messaging.MessageFormatter().format("menu.return-to-options"))))) {
                gamePlayer.getP().closeInventory();
                if (gamePlayer.getGame().getState() == GameState.PREGAME) {
                    SkyWarsReloaded.get().getServer().getScheduler().scheduleSyncDelayedTask(SkyWarsReloaded.get(), () -> new MainMenu(gamePlayer), 2);
                }
                return;
            }

            if (name.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', new Messaging.MessageFormatter().format("color.normal"))))) {
                gamePlayer.getP().closeInventory();
                gamePlayer.setGlass("normal");
                gamePlayer.getGame().setGlass(gamePlayer);
                if (gamePlayer.getGame().getState() == GameState.PREGAME) {
                    SkyWarsReloaded.get().getServer().getScheduler().scheduleSyncDelayedTask(SkyWarsReloaded.get(), () -> new MainMenu(gamePlayer), 2);
                }
                return;
            }

            GlassColor glass = SkyWarsReloaded.getGLC().getByName(name);
            if (glass == null) {
                return;
            }

            if (!hasColorPermission(gamePlayer, glass)) {
                gamePlayer.getP().closeInventory();
            } else {
                gamePlayer.setGlass(glass.getColor());
                gamePlayer.getGame().setGlass(glass.getMaterial(), glass.getData(), gamePlayer);
                gamePlayer.getP().closeInventory();
                if (gamePlayer.getGame().getState() == GameState.PREGAME) {
                    SkyWarsReloaded.get().getServer().getScheduler().scheduleSyncDelayedTask(SkyWarsReloaded.get(), () -> new MainMenu(gamePlayer), 2);
                }
            }
        });

        ArrayList<Integer> placement = new ArrayList<>(Arrays.asList(0, 2, 4, 6, 8, 9, 11, 13, 15, 17, 18, 20, 22, 24, 26, 27));

        for (int iii = 0; iii < availableItems.size(); iii++) {
            if (iii >= menuSize) {
                break;
            }

            GlassColor glass = availableItems.get(iii);
            List<String> loreList = Lists.newLinkedList();

            if (hasColorPermission(gamePlayer, glass)) {
                loreList.add(new Messaging.MessageFormatter().format("menu.usecolor-setcolor"));
            } else {
                loreList.add(new Messaging.MessageFormatter().format("menu.usecolor-no-use"));
            }

            if (gamePlayer.getP() != null) {
                SkyWarsReloaded.getIC().setOption(
                        gamePlayer.getP(),
                        placement.get(iii),
                        glass.getItem().clone(),
                        glass.getName(),
                        loreList.toArray(new String[loreList.size()]));
            }
        }

        List<String> loreList2 = Lists.newLinkedList();
        loreList2.add(new Messaging.MessageFormatter().format("menu.usecolor-setcolor"));
        SkyWarsReloaded.getIC().setOption(
                gamePlayer.getP(),
                29,
                new ItemStack(Material.GLASS, 1),
                new Messaging.MessageFormatter().format("colors.normal"),
                loreList2.toArray(new String[loreList2.size()]));

        List<String> loreList5 = Lists.newLinkedList();
        SkyWarsReloaded.getIC().setOption(
                gamePlayer.getP(),
                35,
                new ItemStack(Material.TORCH, 1),
                new Messaging.MessageFormatter().format("menu.return-to-options"),
                loreList5.toArray(new String[loreList5.size()]));

        if (gamePlayer.getP() != null) {
            SkyWarsReloaded.getIC().show(gamePlayer.getP());
        }
    }

    private boolean hasColorPermission(GamePlayer player, GlassColor color) {
        return (player.getP().isOp() || player.getP().hasPermission(premissionPrefix + color.getColor()) || player.hasPerm(premissionPrefix + color.getColor()));
    }
}
