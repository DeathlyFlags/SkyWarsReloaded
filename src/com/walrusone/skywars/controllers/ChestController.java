package com.walrusone.skywars.controllers;

import com.google.common.collect.Lists;
import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.utilities.ItemUtils;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class ChestController {

    private final List<ChestItem> chestItemList = Lists.newArrayList();
    private final List<ChestItem> opChestItemList = Lists.newArrayList();
    private final List<ChestItem> basicChestItemList = Lists.newArrayList();
    private final Random random = new Random();

    private final List<Integer> randomLoc = new ArrayList<>();
    private final List<Integer> randomDLoc = new ArrayList<>();


    public ChestController() {
        load();
        for (int i = 0; i < 27; i++) {
            randomLoc.add(i);
        }
        for (int i = 0; i < 54; i++) {
            randomDLoc.add(i);
        }
    }

    private void load() {
        chestItemList.clear();
        File chestFile = new File(SkyWarsReloaded.get().getDataFolder(), "chest.yml");

        if (!chestFile.exists()) {
            SkyWarsReloaded.get().saveResource("chest.yml", false);
        }

        if (chestFile.exists()) {
            loadChest(chestFile);
        }

        opChestItemList.clear();
        File opChestFile = new File(SkyWarsReloaded.get().getDataFolder(), "opchest.yml");

        if (!opChestFile.exists()) {
            SkyWarsReloaded.get().saveResource("opchest.yml", false);
        }

        if (opChestFile.exists()) {
            loadChest(opChestFile);
        }

        basicChestItemList.clear();
        File basicChestFile = new File(SkyWarsReloaded.get().getDataFolder(), "basicchest.yml");

        if (!basicChestFile.exists()) {
            SkyWarsReloaded.get().saveResource("basicchest.yml", false);
        }

        if (basicChestFile.exists()) {
            loadChest(basicChestFile);
        }

    }

    public void populateChest(Chest chest, String chestfile) {
        Inventory inventory = chest.getBlockInventory();
        inventory.clear();
        int added = 0;
        Collections.shuffle(randomLoc);

        if (chestfile.equalsIgnoreCase("op")) {
            for (ChestItem chestItem : opChestItemList) {
                if (random.nextInt(100) + 1 <= chestItem.getChance()) {
                    inventory.setItem(randomLoc.get(added), chestItem.getItem());
                    if (added++ >= inventory.getSize() - 1) {
                        break;
                    }
                }
            }
        } else if (chestfile.equalsIgnoreCase("basic")) {
            for (ChestItem chestItem : basicChestItemList) {
                if (random.nextInt(100) + 1 <= chestItem.getChance()) {
                    inventory.setItem(randomLoc.get(added), chestItem.getItem());
                    if (added++ >= inventory.getSize() - 1) {
                        break;
                    }
                }
            }
        } else {
            for (ChestItem chestItem : chestItemList) {
                if (random.nextInt(100) + 1 <= chestItem.getChance()) {
                    inventory.setItem(randomLoc.get(added), chestItem.getItem());
                    if (added++ >= inventory.getSize() - 1) {
                        break;
                    }
                }
            }
        }
    }

    public void populateDoubleChest(DoubleChest chest, String chestfile) {
        String type = chestfile;
        if (SkyWarsReloaded.getCfg().doubleChestAlwaysOP())
            type = "op";

        Inventory inventory = chest.getInventory();
        inventory.clear();
        int added = 0;
        Collections.shuffle(randomDLoc);


        if (type.equalsIgnoreCase("op")) {
            for (ChestItem chestItem : opChestItemList) {
                if (random.nextInt(100) + 1 <= chestItem.getChance()) {
                    inventory.setItem(randomDLoc.get(added), chestItem.getItem());
                    if (added++ >= inventory.getSize() - 1) {
                        break;
                    }
                }
            }
        } else if (type.equalsIgnoreCase("basic")) {
            for (ChestItem chestItem : basicChestItemList) {
                if (random.nextInt(100) + 1 <= chestItem.getChance()) {
                    inventory.setItem(randomDLoc.get(added),
                            chestItem.getItem());
                    if (added++ >= inventory.getSize() - 1) {
                        break;
                    }
                }
            }
        } else {
            for (ChestItem chestItem : chestItemList) {
                if (random.nextInt(100) + 1 <= chestItem.getChance()) {
                    inventory.setItem(randomDLoc.get(added),
                            chestItem.getItem());
                    if (added++ >= inventory.getSize() - 1) {
                        break;
                    }
                }
            }
        }
    }

    private void loadChest(File chest) {
        FileConfiguration storage = YamlConfiguration.loadConfiguration(chest);

        if (storage.contains("items")) {
            for (String item : storage.getStringList("items")) {
                List<String> itemData = new LinkedList<>(Arrays.asList(item.split(" ")));

                int chance = Integer.parseInt(itemData.get(0));
                itemData.remove(itemData.get(0));

                ItemStack itemStack = ItemUtils.parseItem(itemData);


                if (itemStack != null) {
                    if (chest.getName().equalsIgnoreCase("opchest.yml"))
                        opChestItemList.add(new ChestItem(itemStack, chance));
                    else if (chest.getName().equals("basicchest.yml"))
                        basicChestItemList.add(new ChestItem(itemStack, chance));
                    else if (chest.getName().equals("chest.yml"))
                        chestItemList.add(new ChestItem(itemStack, chance));
                }
            }
        }
    }

    private class ChestItem {

        private final ItemStack item;
        private final int chance;

        public ChestItem(ItemStack item, int chance) {
            this.item = item;
            this.chance = chance;
        }

        public ItemStack getItem() {
            return item;
        }

        public int getChance() {
            return chance;
        }
    }

}