package com.walrusone.skywars.powerups;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.walrusone.skywars.SkyWarsReloaded;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

class Jump {

    public static void dropPowerUp(Location loc) {
        String line1 = ChatColor.GOLD + "" + ChatColor.BOLD + "Jump II";
        String line2 = ChatColor.GOLD + "" + ChatColor.BOLD + "20 segundos";
        ItemStack icon = new ItemStack(Material.FEATHER);

        final Hologram hologram = HologramsAPI.createHologram(
                SkyWarsReloaded.get(), loc.add(0.0, 0.6, 0.0));
        hologram.appendTextLine(line1);
        hologram.appendTextLine(line2);

        ItemLine itemLine = hologram.appendItemLine(icon);

        itemLine.setPickupHandler(player -> {

            // Play a sound
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 2F);

            // Play an effect
            player.playEffect(hologram.getLocation(), Effect.CLOUD, null);

            // 1 minute of fire resistance
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,
                    20 * 20, 1));

            // Delete the hologram
            hologram.delete();
        });
    }
}
