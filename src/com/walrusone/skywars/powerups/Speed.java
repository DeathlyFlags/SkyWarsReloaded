package com.walrusone.skywars.powerups;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.walrusone.skywars.SkyWarsReloaded;

public class Speed {

	public static void dropPowerUp(Location loc) {
		String line1 = ChatColor.GOLD + "" + ChatColor.BOLD + "Velocidad II";
		String line2 = ChatColor.GOLD + "" + ChatColor.BOLD + "20 segundos";
		ItemStack icon = new ItemStack(Material.MINECART);

		final Hologram hologram = HologramsAPI.createHologram(
				SkyWarsReloaded.get(), loc.add(0.0, 0.6, 0.0));
		hologram.appendTextLine(line1);
		hologram.appendTextLine(line2);

		ItemLine itemLine = hologram.appendItemLine(icon);

		itemLine.setPickupHandler(new PickupHandler() {
			@Override
			public void onPickup(Player player) {

				// Play a sound
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 2F);

				// Play an effect
				player.playEffect(hologram.getLocation(), Effect.SMALL_SMOKE,
						null);

				// 1 minute of fire resistance
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
						20 * 20, 1));

				// Delete the hologram
				hologram.delete();
			}
		});
	}
}
