package com.walrusone.skywars.game;

import com.google.common.collect.Lists;
import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.utilities.ItemUtils;
import com.walrusone.skywars.utilities.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GameKit {

    private final List<ItemStack> items = Lists.newArrayList();
    private final List<PotionEffect> potionEffects = Lists.newArrayList();
    private String name;
    private String kitName;
    private int permCost;
    private int cost;
    private ItemStack icon;
    private int position;
    private List<String> lores;

    @SuppressWarnings("deprecation")
    public GameKit(String name, FileConfiguration storage, File kit) {
        try {
            this.name = name;

            List<String> itemDatas = storage.getStringList("items");
            for (String itemData : itemDatas) {

                List<String> item = Arrays.asList(itemData.split(" "));
                ItemStack itemStack = ItemUtils.parseItem(item);

                if (itemStack != null) {
                    items.add(itemStack);
                }
            }

            List<String> effects = storage.getStringList("potionEffects");
            for (String effect : effects) {

                List<String> effectDetails = Arrays.asList(effect.split(" "));
                PotionEffect potionEffect = ItemUtils
                        .parseEffect(effectDetails);

                if (potionEffect != null) {
                    potionEffects.add(potionEffect);
                }
            }

            if (storage.getString("kitName") != null) {
                kitName = storage.getString("kitName");
            } else {
                kitName = name;
                storage.set("kitName", name);
                try {
                    storage.save(kit);
                } catch (IOException ignored) {
                }
            }

            if (storage.getString("permCost") != null) {
                permCost = storage.getInt("permCost");
            } else {
                permCost = 1000000;
                storage.set("permCost", permCost);
                try {
                    storage.save(kit);
                } catch (IOException ignored) {
                }
            }

            cost = storage.getInt("cost", 0);
            position = storage.getInt("menuPostion");

            Material material = ItemUtils.isInteger(storage.getString("icon")) ? Material
                    .getMaterial(new Integer(storage.getString("icon")))
                    : Material.getMaterial(storage.getString("icon")
                    .toUpperCase());

            if (material == null) {
                material = Material.STONE;
            }

            this.icon = new ItemStack(material, 1);

            lores = Lists.newLinkedList();
            if (storage.contains("details")) {
                lores.addAll(storage.getStringList("details").stream().map(string -> ChatColor.translateAlternateColorCodes('&',
                        string)).collect(Collectors.toList()));
            }

            if (SkyWarsReloaded.getCfg().showKitItemsandPotionEffects()) {
                String contents = new Messaging.MessageFormatter()
                        .format("kits.contents");
                lores.add(contents);
                for (ItemStack itemStack : items) {
                    String enchanted = "";
                    if (ItemUtils.isEnchanted(itemStack)) {
                        enchanted = "Enchanted ";
                    }
                    lores.add(ChatColor.YELLOW + enchanted + ChatColor.WHITE
                            + "" + SkyWarsReloaded.getNMS().getName(itemStack));
                }
                lores.add(ChatColor.DARK_BLUE + " ");
                String potions = new Messaging.MessageFormatter()
                        .format("kits.potion-effects");
                if (potionEffects.size() > 0) {
                    lores.add(potions);
                    lores.addAll(potionEffects.stream().map(potionEffect -> ChatColor.WHITE + ""
                            + potionEffect.getType().getName() + ", "
                            + potionEffect.getDuration() + ", "
                            + potionEffect.getAmplifier()).collect(Collectors.toList()));
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            SkyWarsReloaded
                    .get()
                    .getLogger()
                    .info("There is an error in the kit: " + ChatColor.RED
                            + name);
        }

    }

    public Collection<ItemStack> getItems() {
        return items;
    }

    public Collection<PotionEffect> getPotionEffects() {
        return potionEffects;
    }

    public String getName() {
        return name;
    }

    public String getKitName() {
        return kitName;
    }

    public int getCost() {
        return cost;
    }

    public int getPermCost() {
        return permCost;
    }

    public int getPosition() {
        return position;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public List<String> getLores() {
        return lores;
    }
}
