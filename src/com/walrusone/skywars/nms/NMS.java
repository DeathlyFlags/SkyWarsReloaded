package com.walrusone.skywars.nms;

import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface NMS {

    void respawnPlayer(Player player);

    void updateChunks(List<Chunk> chunks);

    void sendParticles(World world, String type, float x, float y, float z, float offsetX, float offsetY, float offsetZ, int amount);

    String getName(ItemStack stack);

    FireworkEffect getFireworkEffect(Color one, Color two, Color three, Color four, Color five, Type type);

    void sendTitle(Player player, String title, String subtitle);
}
