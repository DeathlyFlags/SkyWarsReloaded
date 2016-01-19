package com.walrusone.skywars.controllers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.game.GameKit;
import com.walrusone.skywars.game.GamePlayer;
import com.walrusone.skywars.utilities.Util;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;
import java.util.Map;

public class KitController {

    private final Map<String, GameKit> kitMap = Maps.newHashMap();

    public KitController() {
        load();
    }

    private void load() {
        kitMap.clear();
        File dataDirectory = SkyWarsReloaded.get().getDataFolder();
        File kitsDirectory = new File(dataDirectory, "kits");

        if (!kitsDirectory.exists()) {
            if (!kitsDirectory.mkdirs()) {
                return;
            }
            SkyWarsReloaded.get().saveResource("example.yml", true);
            Util.copyFiles(new File(dataDirectory, "example.yml"), new File(kitsDirectory, "example.yml"));
            File delete = new File(dataDirectory, "example.yml");
            delete.delete();
        }

        File[] kits = kitsDirectory.listFiles();
        if (kits == null) {
            return;
        }

        for (File kit : kits) {
            if (!kit.getName().endsWith(".yml")) {
                continue;
            }

            String name = kit.getName().replace(".yml", "");

            if (!name.isEmpty() && !kitMap.containsKey(name)) {
                kitMap.put(name, new GameKit(name, YamlConfiguration.loadConfiguration(kit), kit));
            }
        }
    }

    public void populateInventory(Inventory inventory, GameKit kit) {
        for (ItemStack itemStack : kit.getItems()) {
            try {
                inventory.addItem(itemStack);
            } catch (NullPointerException ignored) {
            }

        }
    }

    public void givePotionEffects(GamePlayer gamePlayer, GameKit kit) {
        kit.getPotionEffects().stream().filter(pEffect -> gamePlayer.getP() != null).forEach(pEffect -> gamePlayer.getP().addPotionEffect(pEffect));
    }

    public GameKit getByName(String name) {
        for (String kitName : kitMap.keySet()) {
            if (kitMap.get(kitName).getKitName().equalsIgnoreCase(name)) {
                return kitMap.get(kitName);
            }
        }
        return null;
    }

    public List<GameKit> getKits() {
        return Lists.newArrayList(kitMap.values());
    }

}
