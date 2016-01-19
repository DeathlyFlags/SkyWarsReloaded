package com.walrusone.skywars.menus;

import com.google.common.collect.Lists;
import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.game.Game.GameState;
import com.walrusone.skywars.game.GamePlayer;
import com.walrusone.skywars.utilities.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MainMenu {

    private static final int menuSlotsPerRow = 9;
    private static final int menuSize = 36;
    private static final String optionsMenuName = new Messaging.MessageFormatter().format("menu.options-menu-title");

    public MainMenu(final GamePlayer gamePlayer) {

        int rowCount = menuSlotsPerRow;
        while (rowCount < 36 && rowCount < menuSize) {
            rowCount += menuSlotsPerRow;
        }

        SkyWarsReloaded.getIC().create(gamePlayer.getP(), optionsMenuName, rowCount, event -> {
            if (!gamePlayer.inGame() || gamePlayer.getGame().getState() != GameState.PREGAME) {
                return;
            }

            String option = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', event.getName()));

            event.setWillClose(false);
            event.setWillDestroy(false);

            if (option.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', new Messaging.MessageFormatter().format("menu.time-item-name"))))) {
                if (hasTimePermission(gamePlayer.getP())) {
                    gamePlayer.getP().closeInventory();
                    SkyWarsReloaded.get().getServer().getScheduler().scheduleSyncDelayedTask(SkyWarsReloaded.get(), () -> new TimeMenu(gamePlayer), 2);
                } else {
                }
            } else if (option.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', new Messaging.MessageFormatter().format("menu.weather-item-name"))))) {
                if (hasTimePermission(gamePlayer.getP())) {
                    gamePlayer.getP().closeInventory();
                    SkyWarsReloaded.get().getServer().getScheduler().scheduleSyncDelayedTask(SkyWarsReloaded.get(), () -> new WeatherMenu(gamePlayer), 2);
                } else {
                }
            } else if (option.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', new Messaging.MessageFormatter().format("menu.jump-item-name"))))) {
                if (hasJumpPermission(gamePlayer.getP())) {
                    gamePlayer.getP().closeInventory();
                    SkyWarsReloaded.get().getServer().getScheduler().scheduleSyncDelayedTask(SkyWarsReloaded.get(), () -> new JumpMenu(gamePlayer), 2);
                } else {
                }
            } else if (option.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', new Messaging.MessageFormatter().format("menu.chest-item-name"))))) {
                if (hasChestPermission(gamePlayer.getP())) {
                    gamePlayer.getP().closeInventory();
                    SkyWarsReloaded.get().getServer().getScheduler().scheduleSyncDelayedTask(SkyWarsReloaded.get(), () -> new ChestMenu(gamePlayer), 2);
                } else {
                }
            } else if (option.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', new Messaging.MessageFormatter().format("menu.selecteffect-item-name"))))) {
                if (hasEffectPermission(gamePlayer.getP())) {
                    gamePlayer.getP().closeInventory();
                    SkyWarsReloaded.get().getServer().getScheduler().scheduleSyncDelayedTask(SkyWarsReloaded.get(), () -> new UseEffectMenu(gamePlayer), 2);
                } else {
                }
            } else if (option.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', new Messaging.MessageFormatter().format("menu.selectprojeffect-item-name"))))) {
                if (hasEffectPermission(gamePlayer.getP())) {
                    gamePlayer.getP().closeInventory();
                    SkyWarsReloaded.get().getServer().getScheduler().scheduleSyncDelayedTask(SkyWarsReloaded.get(), () -> new UseProjEffectMenu(gamePlayer), 2);
                } else {
                }
            } else if (option.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', new Messaging.MessageFormatter().format("menu.selectcolor-item-name"))))) {
                if (hasColorPermission(gamePlayer.getP())) {
                    gamePlayer.getP().closeInventory();
                    SkyWarsReloaded.get().getServer().getScheduler().scheduleSyncDelayedTask(SkyWarsReloaded.get(), () -> new UseColorMenu(gamePlayer), 2);
                } else {
                }
            } else if (option.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', new Messaging.MessageFormatter().format("menu.exit-menu"))))) {
                gamePlayer.getP().closeInventory();
            } else {
            }
        });

        List<String> chestLore = Lists.newLinkedList();
        List<String> timeLore = Lists.newLinkedList();
        List<String> weatherLore = Lists.newLinkedList();
        List<String> jumpLore = Lists.newLinkedList();
        List<String> colorLore = Lists.newLinkedList();
        List<String> effectLore = Lists.newLinkedList();
        List<String> trailLore = Lists.newLinkedList();

        if (hasTimePermission(gamePlayer.getP())) {
        } else {
            timeLore.add(new Messaging.MessageFormatter().format("error.no-permission-time"));
        }

        if (hasJumpPermission(gamePlayer.getP())) {
        } else {
            jumpLore.add(new Messaging.MessageFormatter().format("error.no-permission-jump"));
        }

        if (hasChestPermission(gamePlayer.getP())) {
        } else {
            chestLore.add(new Messaging.MessageFormatter().format("error.no-permission-chest"));
        }

        if (hasColorPermission(gamePlayer.getP())) {
        } else {
            colorLore.add(new Messaging.MessageFormatter().format("error.no-permission-color"));
        }

        if (hasEffectPermission(gamePlayer.getP())) {
        } else {
            effectLore.add(new Messaging.MessageFormatter().format("error.no-permission-effect"));
        }

        if (hasProjEffectPermission(gamePlayer.getP())) {
        } else {
            trailLore.add(new Messaging.MessageFormatter().format("error.no-permission-projeffect"));
        }

        if (hasWeatherPermission(gamePlayer.getP())) {
        } else {
            weatherLore.add(new Messaging.MessageFormatter().format("error.no-permission-weather"));
        }

        if (gamePlayer.getP() != null && gamePlayer.inGame() && gamePlayer.getGame().getState() == GameState.PREGAME) {
            if (SkyWarsReloaded.getCfg().opChestsEnabled()) {
                SkyWarsReloaded.getIC().setOption(
                        gamePlayer.getP(),
                        SkyWarsReloaded.getCfg().getOpChestOptionMenuSlot(),
                        SkyWarsReloaded.getCfg().getOpChestMenuItem(),
                        new Messaging.MessageFormatter().format("menu.chest-item-name"),
                        chestLore.toArray(new String[chestLore.size()]));
            }

            if (SkyWarsReloaded.getCfg().timeVoteEnabled()) {
                SkyWarsReloaded.getIC().setOption(
                        gamePlayer.getP(),
                        SkyWarsReloaded.getCfg().getTimeOptionMenuSlot(),
                        SkyWarsReloaded.getCfg().getTimeMenuItem(),
                        new Messaging.MessageFormatter().format("menu.time-item-name"),
                        timeLore.toArray(new String[timeLore.size()]));
            }

            if (SkyWarsReloaded.getCfg().jumpVoteEnabled()) {
                SkyWarsReloaded.getIC().setOption(
                        gamePlayer.getP(),
                        SkyWarsReloaded.getCfg().getJumpOptionMenuSlot(),
                        SkyWarsReloaded.getCfg().getJumpMenuItem(),
                        new Messaging.MessageFormatter().format("menu.jump-item-name"),
                        jumpLore.toArray(new String[jumpLore.size()]));
            }

            if (SkyWarsReloaded.getCfg().weatherVoteEnabled()) {
                SkyWarsReloaded.getIC().setOption(
                        gamePlayer.getP(),
                        SkyWarsReloaded.getCfg().getWeatherOptionMenuSlot(),
                        SkyWarsReloaded.getCfg().getWeatherMenuItem(),
                        new Messaging.MessageFormatter().format("menu.weather-item-name"),
                        weatherLore.toArray(new String[weatherLore.size()]));
            }

            if (SkyWarsReloaded.getCfg().colorGlassEnabled()) {
                SkyWarsReloaded.getIC().setOption(
                        gamePlayer.getP(),
                        SkyWarsReloaded.getCfg().getGlassOptionsMenuSlot(),
                        SkyWarsReloaded.getCfg().getGlassMenuItem(),
                        new Messaging.MessageFormatter().format("menu.selectcolor-item-name"),
                        colorLore.toArray(new String[colorLore.size()]));
            }

            if (SkyWarsReloaded.getCfg().particlesEnabled()) {
                SkyWarsReloaded.getIC().setOption(
                        gamePlayer.getP(),
                        SkyWarsReloaded.getCfg().getParticleOptionsMenuSlot(),
                        SkyWarsReloaded.getCfg().getparticleMenuItem(),
                        new Messaging.MessageFormatter().format("menu.selecteffect-item-name"),
                        effectLore.toArray(new String[effectLore.size()]));
            }

            if (SkyWarsReloaded.getCfg().trailEffectsEnabled()) {
                SkyWarsReloaded.getIC().setOption(
                        gamePlayer.getP(),
                        SkyWarsReloaded.getCfg().getTrailEffectOptionsMenuSlot(),
                        SkyWarsReloaded.getCfg().getTrailEffectsMenuItem(),
                        new Messaging.MessageFormatter().format("menu.selectprojeffect-item-name"),
                        trailLore.toArray(new String[trailLore.size()]));
            }

            List<String> loreList5 = Lists.newLinkedList();
            SkyWarsReloaded.getIC().setOption(
                    gamePlayer.getP(),
                    35,
                    new ItemStack(Material.TORCH, 1),
                    new Messaging.MessageFormatter().format("menu.exit-menu"),
                    loreList5.toArray(new String[loreList5.size()]));


            SkyWarsReloaded.getIC().show(gamePlayer.getP());
        }
    }

    private boolean hasTimePermission(Player player) {
        return player.isOp() || player.hasPermission("swr.timevote");
    }

    private boolean hasWeatherPermission(Player player) {
        return player.isOp() || player.hasPermission("swr.weathervote");
    }

    private boolean hasJumpPermission(Player player) {
        return player.isOp() || player.hasPermission("swr.jumpvote");
    }

    private boolean hasChestPermission(Player player) {
        return player.isOp() || player.hasPermission("swr.opchest");
    }

    private boolean hasColorPermission(Player player) {
        return player.isOp() || player.hasPermission("swr.usecolor");
    }

    private boolean hasEffectPermission(Player player) {
        return player.isOp() || player.hasPermission("swr.useeffect");
    }

    private boolean hasProjEffectPermission(Player player) {
        return player.isOp() || player.hasPermission("swr.useprojeffect");
    }
}
