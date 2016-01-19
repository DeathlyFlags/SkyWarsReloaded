package com.walrusone.skywars.utilities;

import org.bukkit.inventory.ItemStack;

public class ShopItem {

    private final ItemStack item;
    private final int cost;

    public ShopItem(ItemStack item, int cost) {
        this.item = item;
        this.cost = cost;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getCost() {
        return cost;
    }

}
