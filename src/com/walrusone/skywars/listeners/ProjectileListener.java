package com.walrusone.skywars.listeners;

import com.walrusone.skywars.SkyWarsReloaded;
import com.walrusone.skywars.game.GamePlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ProjectileListener implements Listener {

    @EventHandler
    public void projectileLaunch(ProjectileLaunchEvent e) {
        Projectile projectile = e.getEntity();
        if (projectile instanceof Snowball || projectile instanceof Egg || projectile instanceof Arrow) {
            if (projectile.getShooter() instanceof Player) {
                GamePlayer shooter = SkyWarsReloaded.getPC().getPlayer(((Player) projectile.getShooter()).getUniqueId());
                String effect = shooter.getProjEffect();
                if (effect != null) {
                    if (!effect.equalsIgnoreCase("normal") && shooter.inGame()) {
                        SkyWarsReloaded.getProjC().addProjectile(projectile, shooter.getProjEffect());
                    }
                }
            }
        }
    }
}
