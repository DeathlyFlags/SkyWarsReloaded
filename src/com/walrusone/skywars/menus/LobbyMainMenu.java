package com.walrusone.skywars.menus;

import com.google.common.collect.Lists;
import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.game.GamePlayer;
import com.walrusone.skywars.utilities.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LobbyMainMenu {

    private static final int menuSlotsPerRow = 9;
    private static final int menuSize = 36;
    private static final String optionsMenuName = new Messaging.MessageFormatter().format("menu.lobbymenu-menu-title");

    public LobbyMainMenu(final GamePlayer gamePlayer) {

        int rowCount = menuSlotsPerRow;
        while (rowCount < 36 && rowCount < menuSize) {
            rowCount += menuSlotsPerRow;
        }

        SkyWarsReloaded.getIC().create(gamePlayer.getP(), optionsMenuName, rowCount, event -> {
            if (gamePlayer.inGame()) {
                gamePlayer.getP().closeInventory();
                return;
            }

            String option = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', event.getName()));

            event.setWillClose(false);
            event.setWillDestroy(false);

            if (option.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', new Messaging.MessageFormatter().format("menu.permkit-item-name"))))) {
                if (hasPermKitPermission(gamePlayer.getP())) {
                    gamePlayer.getP().closeInventory();
                    SkyWarsReloaded.get().getServer().getScheduler().scheduleSyncDelayedTask(SkyWarsReloaded.get(), () -> new PermKitMenu(gamePlayer), 2);
                } else {
                }
            } else if (option.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', new Messaging.MessageFormatter().format("menu.buycolor-item-name"))))) {
                if (hasColorPermission(gamePlayer.getP())) {
                    gamePlayer.getP().closeInventory();
                    SkyWarsReloaded.get().getServer().getScheduler().scheduleSyncDelayedTask(SkyWarsReloaded.get(), () -> new BuyColorMenu(gamePlayer), 2);
                } else {
                }
            } else if (option.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', new Messaging.MessageFormatter().format("menu.buyeffect-item-name"))))) {
                if (hasEffectPermission(gamePlayer.getP())) {
                    gamePlayer.getP().closeInventory();
                    SkyWarsReloaded.get().getServer().getScheduler().scheduleSyncDelayedTask(SkyWarsReloaded.get(), () -> new BuyEffectMenu(gamePlayer), 2);
                } else {
                }
            } else if (option.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', new Messaging.MessageFormatter().format("menu.buyprojeffect-item-name"))))) {
                if (hasEffectPermission(gamePlayer.getP())) {
                    gamePlayer.getP().closeInventory();
                    SkyWarsReloaded.get().getServer().getScheduler().scheduleSyncDelayedTask(SkyWarsReloaded.get(), () -> new BuyProjEffectMenu(gamePlayer), 2);
                } else {
                }
            } else if (option.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', new Messaging.MessageFormatter().format("menu.exit-lobby-menu"))))) {
                gamePlayer.getP().closeInventory();
            } else {
            }
        });

        List<String> kitLore = Lists.newLinkedList();
        if (hasPermKitPermission(gamePlayer.getP())) {
        } else {
            kitLore.add(new Messaging.MessageFormatter().format("error.no-permission-permkits"));
        }

        List<String> colorLore = Lists.newLinkedList();
        if (hasColorPermission(gamePlayer.getP())) {
        } else {
            colorLore.add(new Messaging.MessageFormatter().format("error.no-permission-color-shop"));
        }

        List<String> particleLore = Lists.newLinkedList();
        if (hasEffectPermission(gamePlayer.getP())) {
        } else {
            particleLore.add(new Messaging.MessageFormatter().format("error.no-permission-effect-shop"));
        }

        List<String> projectEffectLore = Lists.newLinkedList();
        if (hasProjEffectPermission(gamePlayer.getP())) {
        } else {
            projectEffectLore.add(new Messaging.MessageFormatter().format("error.no-permission-projeffect-shop"));
        }

        if (gamePlayer.getP() != null && !gamePlayer.inGame()) {
            if (SkyWarsReloaded.getCfg().purchasePermanentKitsEnabled()) {
                SkyWarsReloaded.getIC().setOption(
                        gamePlayer.getP(),
                        SkyWarsReloaded.getCfg().getKitPermMenuItemSlot(),
                        SkyWarsReloaded.getCfg().getPermKitMenuItem(),
                        new Messaging.MessageFormatter().format("menu.permkit-item-name"),
                        kitLore.toArray(new String[kitLore.size()]));
            }

            if (SkyWarsReloaded.getCfg().purchaseColorClassEnabled()) {
                SkyWarsReloaded.getIC().setOption(
                        gamePlayer.getP(),
                        SkyWarsReloaded.getCfg().getGlassLobbyMenuSlot(),
                        SkyWarsReloaded.getCfg().getGlassMenuItem(),
                        new Messaging.MessageFormatter().format("menu.buycolor-item-name"),
                        colorLore.toArray(new String[colorLore.size()]));
            }

            if (SkyWarsReloaded.getCfg().purchaseParticlesEnabled()) {
                SkyWarsReloaded.getIC().setOption(
                        gamePlayer.getP(),
                        SkyWarsReloaded.getCfg().getParticleLobbyMenuSlot(),
                        SkyWarsReloaded.getCfg().getparticleMenuItem(),
                        new Messaging.MessageFormatter().format("menu.buyeffect-item-name"),
                        particleLore.toArray(new String[particleLore.size()]));
            }

            if (SkyWarsReloaded.getCfg().purchaseTrailEffectsEnabled()) {
                SkyWarsReloaded.getIC().setOption(
                        gamePlayer.getP(),
                        SkyWarsReloaded.getCfg().getTrailEffectLobbyMenuSlot(),
                        SkyWarsReloaded.getCfg().getTrailEffectsMenuItem(),
                        new Messaging.MessageFormatter().format("menu.buyprojeffect-item-name"),
                        projectEffectLore.toArray(new String[projectEffectLore.size()]));
            }

            List<String> loreList5 = Lists.newLinkedList();
            SkyWarsReloaded.getIC().setOption(
                    gamePlayer.getP(),
                    35,
                    new ItemStack(Material.TORCH, 1),
                    new Messaging.MessageFormatter().format("menu.exit-lobby-menu"),
                    loreList5.toArray(new String[loreList5.size()]));


            if (!gamePlayer.inGame()) {
                SkyWarsReloaded.getIC().show(gamePlayer.getP());
            }
        }
    }

    private boolean hasEffectPermission(Player player) {
        return player.isOp() || player.hasPermission("swr.effectshop");
    }

    private boolean hasProjEffectPermission(Player player) {
        return player.isOp() || player.hasPermission("swr.projeffectshop");
    }

    private boolean hasColorPermission(Player player) {
        return player.isOp() || player.hasPermission("swr.colorshop");
    }

    private boolean hasPermKitPermission(Player player) {
        return player.isOp() || player.hasPermission("swr.permkits");
    }

}
